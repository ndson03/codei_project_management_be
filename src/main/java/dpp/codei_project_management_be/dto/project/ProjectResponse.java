package dpp.codei_project_management_be.dto.project;

import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.service.ProjectFieldCodec;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setDepartmentId(project.getDepartment().getPartId());
        response.setProjectName(project.getProjectName());
        response.setBranch(project.getBranch());
        response.setNotes(project.getNotes());
        response.setTaskManagements(ProjectFieldCodec.decodeStrings(project.getTaskManagements()));
        response.setRepositories(ProjectFieldCodec.decodeStrings(project.getRepositories()));
        response.setPics(ProjectFieldCodec.decodeStrings(project.getPics()));
        response.setDevWhiteList(ProjectFieldCodec.decodeStrings(project.getDevWhiteList()));
        response.setPmUserIds(ProjectFieldCodec.decodeLongs(project.getPmUserIds()));
        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());
        return response;
    }

}

