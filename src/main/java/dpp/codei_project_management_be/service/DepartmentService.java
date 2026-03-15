package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.entity.Department;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.entity.Role;
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
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

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

        return projectRepository.save(project);
    }

    @Transactional
    public Project assignProjectPm(Long projectId, Long userId) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != Role.DEPT_PIC) {
            throw new AccessDeniedException("Only DEPT_PIC is allowed to assign project PM");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        Long departmentId = project.getDepartment().getPartId();
        if (!departmentRepository.existsByPartIdAndDepartmentPicUsername(departmentId, currentUser.getUsername())) {
            throw new AccessDeniedException("Only PIC of the project's department can assign PM");
        }

        User pmUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        if (pmUser.getRole() != Role.PROJECT_PM) {
            throw new IllegalArgumentException("Only PROJECT_PM user can be assigned as PM");
        }

        project.getPms().add(pmUser);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    private void requireDeptPicOfDepartment(User currentUser, Long deptId) {
        if (currentUser.getRole() != Role.DEPT_PIC) {
            throw new AccessDeniedException("Only DEPT_PIC is allowed to create project");
        }

        boolean isPicOfDepartment = departmentRepository
                .existsByPartIdAndDepartmentPicUsername(deptId, currentUser.getUsername());
        if (!isPicOfDepartment) {
            throw new AccessDeniedException("Only PIC of this department can create project");
        }
    }
}

