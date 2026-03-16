package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.department.CreateDepartmentRequest;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUserService currentUserService;
    private final AccessControlService accessControlService;

    @Transactional
    public Department createDepartment(CreateDepartmentRequest request) {
        requireAdmin();

        Department department = new Department();
        department.setPartName(request.getPartName());
        department.setGitPat(request.getGitPat());
        department.setEcodePat(request.getEcodePat());
        department.setGerritUserName(request.getGerritUserName());
        department.setGerritHttpPassword(request.getGerritHttpPassword());
        department.setJiraSecPat(request.getJiraSecPat());
        department.setJiraMxPat(request.getJiraMxPat());
        department.setJiraLaPat(request.getJiraLaPat());

        List<String> pics = resolveDepartmentPics(request.getDepartmentPicUsernames(), request.getDepartmentPicUsername());
        department.setPics(ProjectFieldCodec.encodeStrings(pics));

        return departmentRepository.save(department);
    }

    @Transactional
    public Department updateDepartment(Long deptId, UpdateDepartmentRequest request) {
        requireAdmin();

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + deptId));

        if (request.getPartName() != null) department.setPartName(request.getPartName());
        if (request.getGitPat() != null) department.setGitPat(request.getGitPat());
        if (request.getEcodePat() != null) department.setEcodePat(request.getEcodePat());
        if (request.getGerritUserName() != null) department.setGerritUserName(request.getGerritUserName());
        if (request.getGerritHttpPassword() != null) department.setGerritHttpPassword(request.getGerritHttpPassword());
        if (request.getJiraSecPat() != null) department.setJiraSecPat(request.getJiraSecPat());
        if (request.getJiraMxPat() != null) department.setJiraMxPat(request.getJiraMxPat());
        if (request.getJiraLaPat() != null) department.setJiraLaPat(request.getJiraLaPat());

        if (request.getDepartmentPicUsernames() != null) {
            List<String> pics = resolveDepartmentPics(request.getDepartmentPicUsernames(), null);
            department.setPics(ProjectFieldCodec.encodeStrings(pics));
        } else if (request.getDepartmentPicUsername() != null && !request.getDepartmentPicUsername().isBlank()) {
            List<String> pics = new ArrayList<>(ProjectFieldCodec.decodeStrings(department.getPics()));
            User picUser = resolveUser(request.getDepartmentPicUsername());
            if (accessControlService.isAdmin(picUser)) {
                throw new IllegalArgumentException("Admin user cannot be assigned as Department PIC");
            }
            if (!pics.contains(picUser.getUsername())) {
                pics.add(picUser.getUsername());
            }
            department.setPics(ProjectFieldCodec.encodeStrings(pics));
        }

        return departmentRepository.save(department);
    }

    @Transactional
    public void deleteDepartment(Long deptId) {
        requireAdmin();

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + deptId));

        List<Project> projects = projectRepository.findAllByDepartmentPartId(deptId);
        projectRepository.deleteAll(projects);

        departmentRepository.delete(department);
    }

    private void requireAdmin() {
        User currentUser = currentUserService.getCurrentUser();
        if (!accessControlService.isAdmin(currentUser)) {
            throw new AccessDeniedException("Only ADMIN is allowed to perform this action");
        }
    }

    private User resolveUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    private List<String> resolveDepartmentPics(List<String> usernames, String fallbackUsername) {
        List<String> source = usernames != null ? usernames : new ArrayList<>();
        if (source.isEmpty() && fallbackUsername != null && !fallbackUsername.isBlank()) {
            source.add(fallbackUsername);
        }

        List<String> result = new ArrayList<>();
        for (String username : source) {
            User user = resolveUser(username);
            if (accessControlService.isAdmin(user)) {
                throw new IllegalArgumentException("Admin user cannot be assigned as Department PIC");
            }
            if (!result.contains(user.getUsername())) {
                result.add(user.getUsername());
            }
        }
        return result;
    }
}

