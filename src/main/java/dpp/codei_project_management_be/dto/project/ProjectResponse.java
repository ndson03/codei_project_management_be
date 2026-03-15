package dpp.codei_project_management_be.dto.project;

import dpp.codei_project_management_be.entity.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponse {

    private Long id;
    private Long departmentId;
    private String projectName;
    private String branch;
    private String notes;
    private List<String> taskManagements = new ArrayList<>();
    private List<String> repositories = new ArrayList<>();
    private List<String> pics = new ArrayList<>();
    private List<String> devWhiteList = new ArrayList<>();
    private List<Long> pmUserIds = new ArrayList<>();

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setDepartmentId(project.getDepartment().getPartId());
        response.setProjectName(project.getProjectName());
        response.setBranch(project.getBranch());
        response.setNotes(project.getNotes());
        response.setTaskManagements(new ArrayList<>(project.getTaskManagements()));
        response.setRepositories(new ArrayList<>(project.getRepositories()));
        response.setPics(new ArrayList<>(project.getPics()));
        response.setDevWhiteList(new ArrayList<>(project.getDevWhiteList()));
        response.setPmUserIds(project.getPms().stream().map(user -> user.getId()).toList());
        return response;
    }

}

