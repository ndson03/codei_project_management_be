package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectUpdateInfoRequest;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;

    public ProjectService(ProjectRepository projectRepository, CurrentUserService currentUserService) {
        this.projectRepository = projectRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public Project updateProjectInfo(Long projectId, ProjectUpdateInfoRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != Role.PROJECT_PM) {
            throw new AccessDeniedException("Only PROJECT_PM is allowed to update project info");
        }

        boolean isPmOfProject = projectRepository.existsByIdAndPmsUsername(projectId, currentUser.getUsername());
        if (!isPmOfProject) {
            throw new AccessDeniedException("Only assigned PM can update this project");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        project.setBranch(request.getBranch());
        project.setRepositories(new ArrayList<>(request.getRepositories()));

        return projectRepository.save(project);
    }
}

