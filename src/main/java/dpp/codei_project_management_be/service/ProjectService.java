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

import java.util.ArrayList;
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
        boolean isPmOfProject = accessControlService.isProjectPmOf(currentUser, projectId);
        boolean isPicOfProjectDepartment = projectRepository.existsByIdAndDepartmentDepartmentPicUsername(
                projectId,
                currentUser.getUsername()
        );
        if (!isPmOfProject && !isPicOfProjectDepartment) {
            throw new AccessDeniedException("Only assigned PM can update this project");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        project.setBranch(request.getBranch());
        project.setRepositories(ProjectFieldCodec.encodeStrings(request.getRepositories()));

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        User currentUser = currentUserService.getCurrentUser();
        if (accessControlService.isAdmin(currentUser)) {
            return projectRepository.findAll();
        }
        if (accessControlService.isDepartmentPic(currentUser)) {
            return projectRepository.findAllByDepartmentDepartmentPicUsername(currentUser.getUsername());
        }
        if (accessControlService.isProjectPm(currentUser)) {
            return projectRepository.findAllByPicUsername(currentUser.getUsername());
        }
        return List.of();
    }
}

