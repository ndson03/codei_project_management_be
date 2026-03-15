package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.entity.Department;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.DepartmentRepository;
import dpp.codei_project_management_be.repository.ProjectRepository;
import dpp.codei_project_management_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final AccessControlService accessControlService;

    @Transactional
    public Project createProject(Long deptId, ProjectDataRequest projectData) {
        User currentUser = currentUserService.getCurrentUser();
        requireDeptPicOfDepartment(currentUser, deptId);

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + deptId));

        Project project = new Project();
        project.setDepartment(department);
        project.setProjectName(projectData.getProjectName());
        project.setBranch(projectData.getBranch());
        project.setNotes(projectData.getNotes());
        project.setTaskManagements(ProjectFieldCodec.encodeStrings(projectData.getTaskManagements()));
        project.setRepositories(ProjectFieldCodec.encodeStrings(projectData.getRepositories()));
        project.setPics(ProjectFieldCodec.encodeStrings(projectData.getPics()));
        project.setDevWhiteList(ProjectFieldCodec.encodeStrings(projectData.getDevWhiteList()));
        project.setPmUserIds(ProjectFieldCodec.encodeLongs(resolveUserIds(projectData.getPmUserIds())));

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        User currentUser = currentUserService.getCurrentUser();
        if (accessControlService.isAdmin(currentUser)) {
            return departmentRepository.findAll();
        }
        if (accessControlService.isDepartmentPic(currentUser)) {
            return departmentRepository.findAllByDepartmentPicUsername(currentUser.getUsername());
        }
        return List.of();
    }

    @Transactional
    public Project updateProjectData(Long projectId, ProjectDataRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        boolean canManageProject = accessControlService.isDepartmentPicOf(currentUser, project.getDepartment().getPartId());
        boolean canEditAsPm = accessControlService.isProjectPmOf(currentUser, projectId);

        if (!canManageProject && !canEditAsPm) {
            throw new AccessDeniedException("Only the owning PIC or assigned PM can update this project");
        }

        if (request.getProjectName() != null) project.setProjectName(request.getProjectName());
        if (request.getBranch() != null) project.setBranch(request.getBranch());
        if (request.getNotes() != null) project.setNotes(request.getNotes());
        project.setTaskManagements(ProjectFieldCodec.encodeStrings(request.getTaskManagements()));
        project.setRepositories(ProjectFieldCodec.encodeStrings(request.getRepositories()));
        project.setPics(ProjectFieldCodec.encodeStrings(request.getPics()));
        project.setDevWhiteList(ProjectFieldCodec.encodeStrings(request.getDevWhiteList()));
        if (canManageProject) {
            project.setPmUserIds(ProjectFieldCodec.encodeLongs(resolveUserIds(request.getPmUserIds())));
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        User currentUser = currentUserService.getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        requireDeptPicOfDepartment(currentUser, project.getDepartment().getPartId());

        projectRepository.delete(project);
    }

    private void requireDeptPicOfDepartment(User currentUser, Long deptId) {
        if (!accessControlService.isDepartmentPicOf(currentUser, deptId)) {
            throw new AccessDeniedException("Only PIC of this department can create project");
        }
    }

    private List<Long> resolveUserIds(List<Long> userIds) {
        LinkedHashSet<Long> userIdSet = new LinkedHashSet<>();
        for (Long userId : userIds) {
            resolveUser(userId);
            userIdSet.add(userId);
        }
        return new ArrayList<>(userIdSet);
    }

    private User resolveUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }
}

