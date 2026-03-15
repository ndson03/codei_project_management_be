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
    private String gitPat;
    private String ecodePat;
    private String gerritUserName;
    private String gerritHttpPassword;
    private String jiraSecPat;
    private String jiraMxPat;
    private String jiraLaPat;
    private String departmentPicUsername;

    public static DepartmentResponse from(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setPartId(department.getPartId());
        response.setPartName(department.getPartName());
        response.setGitPat(department.getGitPat());
        response.setEcodePat(department.getEcodePat());
        response.setGerritUserName(department.getGerritUserName());
        response.setGerritHttpPassword(department.getGerritHttpPassword());
        response.setJiraSecPat(department.getJiraSecPat());
        response.setJiraMxPat(department.getJiraMxPat());
        response.setJiraLaPat(department.getJiraLaPat());
        response.setDepartmentPicUsername(
                department.getDepartmentPic() != null ? department.getDepartmentPic().getUsername() : null
        );
        return response;
    }

}

