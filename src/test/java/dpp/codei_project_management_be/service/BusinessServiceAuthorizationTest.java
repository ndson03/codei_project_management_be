package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.dto.project.ProjectUpdateInfoRequest;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.DepartmentRepository;
import dpp.codei_project_management_be.repository.ProjectRepository;
import dpp.codei_project_management_be.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BusinessServiceAuthorizationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private CurrentUserService currentUserService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void assignDeptPic_shouldRejectNonAdminRequester() {
        setAuth("dev", "ROLE_DEVELOPER");

        User current = new User();
        current.setUsername("dev");
        current.setRole(Role.DEVELOPER);
        when(userRepository.findByUsername("dev")).thenReturn(Optional.of(current));

        AdminService adminService = new AdminService(departmentRepository, userRepository, currentUserService);

        assertThrows(AccessDeniedException.class, () -> adminService.assignDeptPic(2L, 1L));
    }

    @Test
    void assignDeptPic_shouldRejectWhenTargetUserIsNotDeptPic() {
        setAuth("admin", "ROLE_ADMIN");

        User admin = new User();
        admin.setUsername("admin");
        admin.setRole(Role.ADMIN);

        User notDeptPic = new User();
        notDeptPic.setId(2L);
        notDeptPic.setRole(Role.DEVELOPER);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(notDeptPic));

        AdminService adminService = new AdminService(departmentRepository, userRepository, currentUserService);

        assertThrows(IllegalArgumentException.class, () -> adminService.assignDeptPic(2L, 1L));
    }

    @Test
    void createProject_shouldRejectRequesterNotPicOfDepartment() {
        setAuth("picA", "ROLE_DEPT_PIC");

        User pic = new User();
        pic.setUsername("picA");
        pic.setRole(Role.DEPT_PIC);

        when(userRepository.findByUsername("picA")).thenReturn(Optional.of(pic));
        when(departmentRepository.existsByPartIdAndDepartmentPicUsername(10L, "picA")).thenReturn(false);

        DepartmentService departmentService = new DepartmentService(
                departmentRepository,
                projectRepository,
                userRepository,
                currentUserService
        );

        assertThrows(AccessDeniedException.class, () -> departmentService.createProject(10L, new ProjectDataRequest()));
    }

    @Test
    void updateProjectInfo_shouldRejectPmNotAssignedToProject() {
        setAuth("pmA", "ROLE_PROJECT_PM");

        User pm = new User();
        pm.setUsername("pmA");
        pm.setRole(Role.PROJECT_PM);

        when(userRepository.findByUsername("pmA")).thenReturn(Optional.of(pm));
        when(projectRepository.existsByIdAndPmsUsername(15L, "pmA")).thenReturn(false);

        ProjectService projectService = new ProjectService(projectRepository, currentUserService);

        ProjectUpdateInfoRequest request = new ProjectUpdateInfoRequest();
        request.setBranch("main");
        request.setRepositories(Collections.singletonList("repo-a"));

        assertThrows(AccessDeniedException.class, () -> projectService.updateProjectInfo(15L, request));
    }

    @Test
    void updateProjectInfo_shouldUpdateWhenAssignedPm() {
        setAuth("pmA", "ROLE_PROJECT_PM");

        User pm = new User();
        pm.setUsername("pmA");
        pm.setRole(Role.PROJECT_PM);

        Project project = new Project();
        project.setId(15L);

        when(userRepository.findByUsername("pmA")).thenReturn(Optional.of(pm));
        when(projectRepository.existsByIdAndPmsUsername(15L, "pmA")).thenReturn(true);
        when(projectRepository.findById(15L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectService projectService = new ProjectService(projectRepository, currentUserService);

        ProjectUpdateInfoRequest request = new ProjectUpdateInfoRequest();
        request.setBranch("release");
        request.setRepositories(Collections.singletonList("repo-a"));

        Project updated = projectService.updateProjectInfo(15L, request);

        assertEquals("release", updated.getBranch());
        assertEquals(Collections.singletonList("repo-a"), updated.getRepositories());
    }

    private void setAuth(String username, String role) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}



