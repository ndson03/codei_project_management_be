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
import java.util.Set;

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
        project.setTaskManagements(new ArrayList<>(projectData.getTaskManagements()));
        project.setRepositories(new ArrayList<>(projectData.getRepositories()));
        project.setPics(new ArrayList<>(projectData.getPics()));
        project.setDevWhiteList(new ArrayList<>(projectData.getDevWhiteList()));
        project.setPms(resolveUsers(projectData.getPmUserIds()));

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
        project.setTaskManagements(new ArrayList<>(request.getTaskManagements()));
        project.setRepositories(new ArrayList<>(request.getRepositories()));
        project.setPics(new ArrayList<>(request.getPics()));
        project.setDevWhiteList(new ArrayList<>(request.getDevWhiteList()));
        if (canManageProject) {
            project.setPms(resolveUsers(request.getPmUserIds()));
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

    private Set<User> resolveUsers(List<Long> userIds) {
        Set<User> users = new LinkedHashSet<>();
        for (Long userId : userIds) {
            users.add(resolveUser(userId));
        }
        return users;
    }

    private User resolveUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }
}

