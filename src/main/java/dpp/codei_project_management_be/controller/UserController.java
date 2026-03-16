package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.user.UserMeResponse;
import dpp.codei_project_management_be.dto.user.UserResponse;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.service.AccessControlService;
import dpp.codei_project_management_be.service.CurrentUserService;
import dpp.codei_project_management_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserService userService;
    private final AccessControlService accessControlService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(required = false) String assignmentType,
            @RequestParam(required = false) Long deptId
    ) {
        List<User> users;
        if ("PIC".equalsIgnoreCase(assignmentType)) {
            users = userService.getUsersForDepartmentPicAssignment(deptId);
        } else {
            users = userService.getAllUsers();
        }

        List<UserResponse> responses = users.stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeResponse> getCurrentUserProfile() {
        User currentUser = currentUserService.getCurrentUser();
        return ResponseEntity.ok(UserMeResponse.from(
                currentUser,
                accessControlService.resolveAccessMode(currentUser),
                accessControlService.getManagedDepartmentIds(currentUser)
        ));
    }
}



