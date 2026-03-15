package dpp.codei_project_management_be.dto.user;

import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String fullname;
    private String email;
    private Role role;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

}


