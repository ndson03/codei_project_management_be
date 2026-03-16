package dpp.codei_project_management_be.dto.department;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateDepartmentRequest {

    private String partName;
    private String gitPat;
    private String ecodePat;
    private String gerritUserName;
    private String gerritHttpPassword;
    private String jiraSecPat;
    private String jiraMxPat;
    private String jiraLaPat;
    private List<String> departmentPicUsernames;
    private String departmentPicUsername;
}

