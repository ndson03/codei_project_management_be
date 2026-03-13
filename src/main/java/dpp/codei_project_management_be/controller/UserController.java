package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.user.UserMeResponse;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CurrentUserService currentUserService;

    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUserProfile() {
        User currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(UserMeResponse.from(currentUser));
    }
}

