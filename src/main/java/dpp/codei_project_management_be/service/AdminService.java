package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.Department;
import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.DepartmentRepository;
import dpp.codei_project_management_be.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public AdminService(
            DepartmentRepository departmentRepository,
            UserRepository userRepository,
            CurrentUserService currentUserService
    ) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public Department createDepartment(Department department) {
        requireAdmin();

        if (department.getPartId() == null) {
            throw new IllegalArgumentException("Department partId is required");
        }

        if (departmentRepository.existsById(department.getPartId())) {
            throw new IllegalArgumentException("Department already exists: " + department.getPartId());
        }

        return departmentRepository.save(department);
    }

    @Transactional
    public Department assignDeptPic(Long userId, Long deptId) {
        requireAdmin();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        if (user.getRole() != Role.DEPT_PIC) {
            throw new IllegalArgumentException("Only DEPT_PIC user can be assigned as department PIC");
        }

        Department department = departmentRepository.findById(deptId)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + deptId));
        department.setDepartmentPic(user);

        return departmentRepository.save(department);
    }

    private void requireAdmin() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only ADMIN is allowed to perform this action");
        }
    }
}

