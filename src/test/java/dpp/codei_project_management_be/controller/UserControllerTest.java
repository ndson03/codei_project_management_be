package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.user.UserMeResponse;
import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.service.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private UserController userController;

    @Test
    void getCurrentUserProfile_shouldReturnCurrentUserInfo() {
        User user = new User();
        user.setId(10L);
        user.setUsername("pm01");
        user.setFullname("Project Manager");
        user.setEmail("pm01@example.com");
        user.setRole(Role.PROJECT_PM);
        user.setPassword("encoded-password");

        when(currentUserService.getCurrentUser()).thenReturn(user);

        ResponseEntity<UserMeResponse> response = userController.getCurrentUserProfile();
        UserMeResponse body = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10L, body.getId());
        assertEquals("pm01", body.getUsername());
        assertEquals("Project Manager", body.getFullname());
        assertEquals("pm01@example.com", body.getEmail());
        assertEquals(Role.PROJECT_PM, body.getRole());
    }
}


