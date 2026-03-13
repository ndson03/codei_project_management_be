package dpp.codei_project_management_be.dto.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectDataRequest {

    private String projectName;
    private String branch;
    private String notes;
    private List<String> taskManagements = new ArrayList<>();
    private List<String> repositories = new ArrayList<>();
    private List<String> pics = new ArrayList<>();
    private List<String> devWhiteList = new ArrayList<>();

    public ProjectDataRequest() {
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
        this.taskManagements = Objects.requireNonNullElseGet(taskManagements, ArrayList::new);
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = Objects.requireNonNullElseGet(repositories, ArrayList::new);
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = Objects.requireNonNullElseGet(pics, ArrayList::new);
    }

    public List<String> getDevWhiteList() {
        return devWhiteList;
    }

    public void setDevWhiteList(List<String> devWhiteList) {
        this.devWhiteList = Objects.requireNonNullElseGet(devWhiteList, ArrayList::new);
    }
}


