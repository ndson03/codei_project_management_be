package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.user.AccessMode;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.DepartmentRepository;
import dpp.codei_project_management_be.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;

    @Value("${app.auth.admin-username:admin}")
    private String adminUsername;


    public boolean isAdmin(User user) {
        return user != null
                && user.getUsername() != null
                && user.getUsername().equalsIgnoreCase(adminUsername);
    }

    public boolean isDepartmentPic(User user) {
        return user != null && departmentRepository.existsByDepartmentPicUsername(user.getUsername());
    }

    public boolean isDepartmentPicOf(User user, Long departmentId) {
        return user != null
                && departmentId != null
                && departmentRepository.existsByPartIdAndDepartmentPicUsername(departmentId, user.getUsername());
    }

    public boolean isProjectPm(User user) {
        return user != null && user.getId() != null && projectRepository.existsByPmUserId(user.getId());
    }

    public boolean isProjectPmOf(User user, Long projectId) {
        return user != null
                && user.getId() != null
                && projectId != null
                && projectRepository.existsByIdAndPmUserId(projectId, user.getId());
    }

    public AccessMode resolveAccessMode(User user) {
        if (isAdmin(user)) {
            return AccessMode.ADMIN;
        }
        if (isDepartmentPic(user)) {
            return AccessMode.PIC;
        }
        if (isProjectPm(user)) {
            return AccessMode.PM;
        }
        return AccessMode.NONE;
    }

    public List<Long> getManagedDepartmentIds(User user) {
        if (user == null) {
            return List.of();
        }
        return departmentRepository.findAllByDepartmentPicUsername(user.getUsername()).stream()
                .map(department -> department.getPartId())
                .toList();
    }

    public List<Long> getManagedProjectIds(User user) {
        if (user == null) {
            return List.of();
        }
        return projectRepository.findAllByPmUserId(user.getId()).stream()
                .map(project -> project.getId())
                .toList();
    }
}