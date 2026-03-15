package dpp.codei_project_management_be.config;

import dpp.codei_project_management_be.repository.DepartmentRepository;
import dpp.codei_project_management_be.repository.ProjectRepository;
import dpp.codei_project_management_be.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiAuthorizationService {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public boolean canCreateProject(Authentication authentication, HttpServletRequest request) {
        if (!hasRole(authentication, "ROLE_DEPT_PIC")) {
            return false;
        }

        Long departmentId = extractSegmentId(request.getRequestURI(), "departments");
        return departmentId != null
                && departmentRepository.existsByPartIdAndDepartmentPicUsername(departmentId, authentication.getName());
    }

    public boolean canUpdateProject(Authentication authentication, HttpServletRequest request) {
        if (!hasRole(authentication, "ROLE_PROJECT_PM")) {
            return false;
        }

        Long projectId = extractSegmentId(request.getRequestURI(), "projects");
        if (projectId == null) {
            return false;
        }

        return userRepository.findByUsername(authentication.getName())
            .map(user -> projectRepository.existsByIdAndPmUserId(projectId, user.getId()))
            .orElse(false);
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }

    private Long extractSegmentId(String uri, String segmentName) {
        String[] parts = uri.split("/");
        for (int i = 0; i < parts.length - 1; i++) {
            if (segmentName.equals(parts[i])) {
                try {
                    return Long.parseLong(parts[i + 1]);
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }
        return null;
    }
}

