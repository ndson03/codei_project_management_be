package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.user.UserMeResponse;
import dpp.codei_project_management_be.dto.user.UserResponse;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.service.CurrentUserService;
import dpp.codei_project_management_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserService userService;

    public UserController(CurrentUserService currentUserService, UserService userService) {
        this.currentUserService = currentUserService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> responses = userService.getAllUsers().stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUserProfile() {
        User currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(UserMeResponse.from(currentUser));
    }
}


