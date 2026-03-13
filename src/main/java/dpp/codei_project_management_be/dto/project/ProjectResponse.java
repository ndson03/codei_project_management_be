package dpp.codei_project_management_be.dto.project;

import dpp.codei_project_management_be.entity.Project;

import java.util.ArrayList;
import java.util.List;

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

    public ProjectResponse() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getTaskManagements() {
        return taskManagements;
    }

    public void setTaskManagements(List<String> taskManagements) {
        this.taskManagements = taskManagements;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getDevWhiteList() {
        return devWhiteList;
    }

    public void setDevWhiteList(List<String> devWhiteList) {
        this.devWhiteList = devWhiteList;
    }

    public List<Long> getPmUserIds() {
        return pmUserIds;
    }

    public void setPmUserIds(List<Long> pmUserIds) {
        this.pmUserIds = pmUserIds;
    }
}

