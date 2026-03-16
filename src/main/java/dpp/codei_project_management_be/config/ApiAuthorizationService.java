package dpp.codei_project_management_be.config;

import dpp.codei_project_management_be.repository.ProjectRepository;
import dpp.codei_project_management_be.service.AccessControlService;
import dpp.codei_project_management_be.service.CurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiAuthorizationService {

    private final AccessControlService accessControlService;
    private final CurrentUserService currentUserService;
    private final ProjectRepository projectRepository;


    public boolean canCreateProject(Authentication authentication, HttpServletRequest request) {
        var user = currentUserService.getCurrentUser();
        if (user == null) {
            return false;
        }

        if (accessControlService.isAdmin(user)) {
            return true;
        }

        Long departmentId = extractSegmentId(request.getRequestURI(), "departments");
        return departmentId != null && accessControlService.isDepartmentPicOf(user, departmentId);
    }

    public boolean canUpdateProject(Authentication authentication, HttpServletRequest request) {
        var user = currentUserService.getCurrentUser();
        if (user == null) {
            return false;
        }

        if (accessControlService.isAdmin(user)) {
            return true;
        }

        Long projectId = extractSegmentId(request.getRequestURI(), "projects");
        if (projectId == null) {
            return false;
        }

        return projectRepository.findById(projectId)
                .map(project -> {
                    boolean isDeptPic = accessControlService.isDepartmentPicOf(user, project.getDepartment().getPartId());
                    boolean isProjectPic = projectRepository.existsByIdAndPicUsername(projectId, user.getUsername());
                    return isDeptPic || isProjectPic;
                })
                .orElse(false);
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

