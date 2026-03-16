package dpp.codei_project_management_be.dto.user;

import dpp.codei_project_management_be.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private String username;
    private String fullname;
    private String email;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        return response;
    }

}


