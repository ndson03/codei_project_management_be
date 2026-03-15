package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.config.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthController authController;

    @Test
    void logout_shouldReturnSuccessMessage() {
        ResponseEntity<Map<String, String>> response = authController.logout();
        Map<String, String> body = Objects.requireNonNull(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logged out successfully", body.get("message"));
    }
}

