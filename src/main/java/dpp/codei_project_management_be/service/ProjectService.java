package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectUpdateInfoRequest;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;
    private final AccessControlService accessControlService;


    @Transactional
    public Project updateProjectInfo(Long projectId, ProjectUpdateInfoRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        boolean isAdmin = accessControlService.isAdmin(currentUser);
        boolean isProjectPic = projectRepository.existsByIdAndPicUsername(projectId, currentUser.getUsername());
        boolean isPicOfProjectDepartment = projectRepository.findById(projectId)
            .map(project -> accessControlService.isDepartmentPicOf(currentUser, project.getDepartment().getPartId()))
            .orElse(false);
        if (!isAdmin && !isProjectPic && !isPicOfProjectDepartment) {
            throw new AccessDeniedException("Only ADMIN, assigned project PIC, or department PIC can update this project");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        project.setBranch(request.getBranch());
        project.setRepositories(ProjectFieldCodec.encodeStrings(request.getRepositories()));

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        // All authenticated users (ADMIN, PIC, USER) can view all projects
        return projectRepository.findAll();
    }
}

