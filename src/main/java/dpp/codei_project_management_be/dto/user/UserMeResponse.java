package dpp.codei_project_management_be.dto.user;

import dpp.codei_project_management_be.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserMeResponse {

    private Long id;
    private String username;
    private String fullname;
    private String email;
    private AccessMode accessMode;
    private List<Long> departmentPicPartIds;
    private List<Long> pmProjectIds;

    public static UserMeResponse from(
            User user,
            AccessMode accessMode,
            List<Long> departmentPicPartIds,
            List<Long> pmProjectIds
    ) {
        UserMeResponse response = new UserMeResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFullname(user.getFullname());
        response.setEmail(user.getEmail());
        response.setAccessMode(accessMode);
        response.setDepartmentPicPartIds(departmentPicPartIds);
        response.setPmProjectIds(pmProjectIds);
        return response;
    }

}


