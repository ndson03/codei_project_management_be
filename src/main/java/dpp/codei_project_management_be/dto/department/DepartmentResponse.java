package dpp.codei_project_management_be.dto.department;

import dpp.codei_project_management_be.entity.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DepartmentResponse {

    private Long partId;
    private String partName;
    private Long departmentPicUserId;

    public static DepartmentResponse from(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setPartId(department.getPartId());
        response.setPartName(department.getPartName());
        response.setDepartmentPicUserId(
                department.getDepartmentPic() != null ? department.getDepartmentPic().getId() : null
        );
        return response;
    }

}

