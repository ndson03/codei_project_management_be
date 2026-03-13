package dpp.codei_project_management_be.dto.department;

import dpp.codei_project_management_be.entity.Department;

public class DepartmentResponse {

    private Long partId;
    private String partName;
    private Long departmentPicUserId;

    public DepartmentResponse() {
    }

    public static DepartmentResponse from(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setPartId(department.getPartId());
        response.setPartName(department.getPartName());
        response.setDepartmentPicUserId(
                department.getDepartmentPic() != null ? department.getDepartmentPic().getId() : null
        );
        return response;
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Long getDepartmentPicUserId() {
        return departmentPicUserId;
    }

    public void setDepartmentPicUserId(Long departmentPicUserId) {
        this.departmentPicUserId = departmentPicUserId;
    }
}

