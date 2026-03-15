package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.dto.department.UpdateDepartmentRequest;
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
        project.setPics(ProjectFieldCodec.encodeStrings(resolveUsernamesForDepartment(deptId, projectData.getPics(), "PIC username")));
        project.setDevWhiteList(ProjectFieldCodec.encodeStrings(resolveUsernamesForDepartment(deptId, projectData.getDevWhiteList(), "Dev whitelist username")));

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
    public Department updateDepartment(Long deptId, UpdateDepartmentRequest request) {
        User currentUser = currentUserService.getCurrentUser();

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + deptId));

        boolean isAdmin = accessControlService.isAdmin(currentUser);
        boolean isOwnerPic = accessControlService.isDepartmentPicOf(currentUser, deptId);
        if (!isAdmin && !isOwnerPic) {
            throw new AccessDeniedException("Only ADMIN or owning PIC can update this department");
        }

        if (request.getPartName() != null) department.setPartName(request.getPartName());
        if (request.getGitPat() != null) department.setGitPat(request.getGitPat());
        if (request.getEcodePat() != null) department.setEcodePat(request.getEcodePat());
        if (request.getGerritUserName() != null) department.setGerritUserName(request.getGerritUserName());
        if (request.getGerritHttpPassword() != null) department.setGerritHttpPassword(request.getGerritHttpPassword());
        if (request.getJiraSecPat() != null) department.setJiraSecPat(request.getJiraSecPat());
        if (request.getJiraMxPat() != null) department.setJiraMxPat(request.getJiraMxPat());
        if (request.getJiraLaPat() != null) department.setJiraLaPat(request.getJiraLaPat());

        if (request.getDepartmentPicUsername() != null && !request.getDepartmentPicUsername().isBlank()) {
            if (!isAdmin) {
                throw new AccessDeniedException("Only ADMIN can reassign department PIC");
            }

            User picUser = userRepository.findById(request.getDepartmentPicUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + request.getDepartmentPicUsername()));
            if (accessControlService.isAdmin(picUser)) {
                throw new IllegalArgumentException("Admin user cannot be assigned as Department PIC");
            }
            if (departmentRepository.existsByDepartmentPicUsernameAndPartIdNot(picUser.getUsername(), deptId)) {
                throw new IllegalArgumentException("User is already PIC of another department");
            }
            department.setDepartmentPic(picUser);
        }

        return departmentRepository.save(department);
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
        project.setDevWhiteList(ProjectFieldCodec.encodeStrings(resolveUsernamesForDepartment(project.getDepartment().getPartId(), request.getDevWhiteList(), "Dev whitelist username")));
        if (canManageProject) {
            // PM semantics are now represented by the pics field.
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

    private List<String> resolveUsernamesForDepartment(Long departmentId, List<String> usernames, String fieldName) {
        if (usernames == null) {
            return List.of();
        }

        LinkedHashSet<String> usernameSet = new LinkedHashSet<>();
        for (String username : usernames) {
            User user = resolveUser(username);
            if (user.getPartId() == null || !user.getPartId().equals(departmentId)) {
                throw new IllegalArgumentException(fieldName + " must belong to the same department");
            }
            usernameSet.add(user.getUsername());
        }
        return new ArrayList<>(usernameSet);
    }

    private User resolveUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }
}

