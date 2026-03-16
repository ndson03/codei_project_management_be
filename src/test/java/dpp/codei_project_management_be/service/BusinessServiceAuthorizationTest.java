package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.dto.department.CreateDepartmentRequest;
import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
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
    void createDepartment_shouldRejectNonAdminRequester() {
        setAuth("dev", "ROLE_USER");

        User current = new User();
        current.setUsername("dev");
        when(userRepository.findByUsername("dev")).thenReturn(Optional.of(current));
        when(accessControlService.isAdmin(current)).thenReturn(false);

        AdminService adminService = new AdminService(
                departmentRepository,
                userRepository,
                projectRepository,
                currentUserService,
                accessControlService
        );

        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setPartName("Dept A");

        assertThrows(AccessDeniedException.class, () -> adminService.createDepartment(request));
    }

    @Test
    void createProject_shouldRejectRequesterNotPicOfDepartment() {
        setAuth("picA", "ROLE_USER");

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
    void updateProjectData_shouldUpdateWhenAssignedProjectPic() {
        setAuth("picA", "ROLE_USER");

        User pic = new User();
        pic.setUsername("picA");

        Department department = new Department();
        department.setPartId(10L);

        Project project = new Project();
        project.setId(15L);
        project.setDepartment(department);
        project.setBranch("main");

        when(userRepository.findByUsername("picA")).thenReturn(Optional.of(pic));
        when(accessControlService.isAdmin(pic)).thenReturn(false);
        when(accessControlService.isDepartmentPicOf(pic, 10L)).thenReturn(false);
        when(projectRepository.existsByIdAndPicUsername(15L, "picA")).thenReturn(true);
        when(projectRepository.findById(15L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepartmentService departmentService = new DepartmentService(
            departmentRepository,
            projectRepository,
            userRepository,
            currentUserService,
            accessControlService
        );

        ProjectDataRequest request = new ProjectDataRequest();
        request.setBranch("release");
        request.setRepositories(Collections.singletonList("repo-a"));

        Project updated = departmentService.updateProjectData(15L, request);

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





