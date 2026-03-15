package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${app.auth.admin-username:admin}")
    private String adminUsername;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
            .authorities(
                user.getRole() == dpp.codei_project_management_be.entity.Role.ADMIN
                    || user.getUsername().equalsIgnoreCase(adminUsername)
                    ? "ROLE_ADMIN"
                    : "ROLE_USER"
            )
                .build();
    }
}

