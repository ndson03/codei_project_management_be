package dpp.codei_project_management_be.config;

import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider,
            ApiAuthorizationService apiAuthorizationService
    ) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/admin/departments").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/admin/departments/*/pic").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/departments/*/projects/**")
                        .access((authentication, context) -> {
                            if (context == null) {
                                return new AuthorizationDecision(false);
                            }
                            return canCreateProject(authentication.get(), context, apiAuthorizationService);
                        })
                        .requestMatchers(HttpMethod.PUT, "/api/projects/*/pm").hasRole(Role.DEPT_PIC.name())
                        .requestMatchers(HttpMethod.PUT, "/api/projects/*")
                        .access((authentication, context) -> {
                            if (context == null) {
                                return new AuthorizationDecision(false);
                            }
                            return canUpdateProject(authentication.get(), context, apiAuthorizationService);
                        })
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService customUserDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    private AuthorizationDecision canCreateProject(
            org.springframework.security.core.Authentication authentication,
            RequestAuthorizationContext context,
            ApiAuthorizationService apiAuthorizationService
    ) {
        HttpServletRequest request = context.getRequest();
        if (authentication == null) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(apiAuthorizationService.canCreateProject(authentication, request));
    }

    private AuthorizationDecision canUpdateProject(
            org.springframework.security.core.Authentication authentication,
            RequestAuthorizationContext context,
            ApiAuthorizationService apiAuthorizationService
    ) {
        HttpServletRequest request = context.getRequest();
        if (authentication == null) {
            return new AuthorizationDecision(false);
        }
        return new AuthorizationDecision(apiAuthorizationService.canUpdateProject(authentication, request));
    }
}



