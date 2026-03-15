package dpp.codei_project_management_be.dto.user;

import dpp.codei_project_management_be.entity.Role;
import dpp.codei_project_management_be.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMeResponse {

    private Long id;
    private String username;
    private String fullname;
    private String email;
    private Role role;

    public static UserMeResponse from(User user) {
        UserMeResponse response = new UserMeResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

}


