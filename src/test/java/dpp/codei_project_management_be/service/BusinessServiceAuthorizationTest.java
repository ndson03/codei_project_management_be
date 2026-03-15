package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.dto.project.ProjectUpdateInfoRequest;
import dpp.codei_project_management_be.entity.Department;
import dpp.codei_project_management_be.entity.Project;
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

    @Mock
    private AccessControlService accessControlService;

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
        when(userRepository.findByUsername("dev")).thenReturn(Optional.of(current));

        AdminService adminService = new AdminService(
                departmentRepository,
                userRepository,
                projectRepository,
            currentUserService,
            accessControlService
        );

        when(accessControlService.isAdmin(current)).thenReturn(false);

        Department department = new Department();
        department.setPartId(1L);

        assertThrows(AccessDeniedException.class, () -> adminService.createDepartment(department));
    }

    @Test
    void createDepartment_shouldAssignExistingPicWhenAdmin() {
        setAuth("admin", "ROLE_ADMIN");

        User admin = new User();
        admin.setUsername("admin");

        User managedUser = new User();
        managedUser.setId(2L);
        managedUser.setUsername("pic-a");

        Department department = new Department();
        department.setPartId(1L);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(userRepository.findById(2L)).thenReturn(Optional.of(managedUser));
        when(departmentRepository.existsById(1L)).thenReturn(false);
        when(departmentRepository.save(any(Department.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(accessControlService.isAdmin(admin)).thenReturn(true);

        AdminService adminService = new AdminService(
                departmentRepository,
                userRepository,
                projectRepository,
            currentUserService,
            accessControlService
        );

        department.setDepartmentPic(managedUser);

        Department updatedDepartment = adminService.createDepartment(department);

        assertEquals(2L, updatedDepartment.getDepartmentPic().getId());
    }

    @Test
    void createProject_shouldRejectRequesterNotPicOfDepartment() {
        setAuth("picA", "ROLE_DEPT_PIC");

        User pic = new User();
        pic.setUsername("picA");

        when(userRepository.findByUsername("picA")).thenReturn(Optional.of(pic));
        when(accessControlService.isDepartmentPicOf(pic, 10L)).thenReturn(false);

        DepartmentService departmentService = new DepartmentService(
                departmentRepository,
                projectRepository,
                userRepository,
            currentUserService,
            accessControlService
        );

        assertThrows(AccessDeniedException.class, () -> departmentService.createProject(10L, new ProjectDataRequest()));
    }

    @Test
    void updateProjectInfo_shouldRejectPmNotAssignedToProject() {
        setAuth("pmA", "ROLE_PROJECT_PM");

        User pm = new User();
        pm.setUsername("pmA");

        when(userRepository.findByUsername("pmA")).thenReturn(Optional.of(pm));
        when(accessControlService.isProjectPmOf(pm, 15L)).thenReturn(false);
        when(projectRepository.existsByIdAndDepartmentDepartmentPicUsername(15L, "pmA")).thenReturn(false);

        ProjectService projectService = new ProjectService(projectRepository, currentUserService, accessControlService);

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

        Project project = new Project();
        project.setId(15L);

        when(userRepository.findByUsername("pmA")).thenReturn(Optional.of(pm));
        when(accessControlService.isProjectPmOf(pm, 15L)).thenReturn(true);
        when(projectRepository.findById(15L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProjectService projectService = new ProjectService(projectRepository, currentUserService, accessControlService);

        ProjectUpdateInfoRequest request = new ProjectUpdateInfoRequest();
        request.setBranch("release");
        request.setRepositories(Collections.singletonList("repo-a"));

        Project updated = projectService.updateProjectInfo(15L, request);

        assertEquals("release", updated.getBranch());
        assertEquals(ProjectFieldCodec.encodeStrings(Collections.singletonList("repo-a")), updated.getRepositories());
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





