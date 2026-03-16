package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.user.AccessMode;
import dpp.codei_project_management_be.entity.Department;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final DepartmentRepository departmentRepository;

    @Value("${app.auth.admin-username:admin}")
    private String adminUsername;

    /**
     * Check if user is admin
     */
    public boolean isAdmin(User user) {
        return user != null
                && user.getUsername() != null
                && user.getUsername().equalsIgnoreCase(adminUsername);
    }

    /**
     * Check if user is PIC of any department
     */
    public boolean isDepartmentPic(User user) {
        if (user == null || user.getUsername() == null) {
            return false;
        }
        List<Department> managedDepts = departmentRepository.findAll();
        return managedDepts.stream()
                .anyMatch(dept -> isPicInJsonArray(dept.getPics(), user.getUsername()));
    }

    /**
     * Check if user is PIC of specific department
     */
    public boolean isDepartmentPicOf(User user, Long departmentId) {
        if (user == null || departmentId == null || user.getUsername() == null) {
            return false;
        }
        return departmentRepository.findById(departmentId)
                .map(dept -> isPicInJsonArray(dept.getPics(), user.getUsername()))
                .orElse(false);
    }

    /**
     * Resolve access mode for user: ADMIN, PIC, or USER
     */
    public AccessMode resolveAccessMode(User user) {
        if (isAdmin(user)) {
            return AccessMode.ADMIN;
        }
        if (isDepartmentPic(user)) {
            return AccessMode.PIC;
        }
        return AccessMode.USER;
    }

    /**
     * Get list of department IDs managed by user as PIC
     */
    public List<Long> getManagedDepartmentIds(User user) {
        if (user == null || user.getUsername() == null) {
            return List.of();
        }
        String username = user.getUsername();
        return departmentRepository.findAll().stream()
                .filter(dept -> isPicInJsonArray(dept.getPics(), username))
                .map(Department::getPartId)
                .toList();
    }

    /**
     * Check if username exists in JSON array string (e.g., "[\"user1\",\"user2\"]")
     */
    private boolean isPicInJsonArray(String jsonArray, String username) {
        if (jsonArray == null || jsonArray.trim().isEmpty() || jsonArray.equals("[]")) {
            return false;
        }
        // Simple check - in production, use Jackson or similar
        return jsonArray.contains("\"" + username + "\"");
    }
}