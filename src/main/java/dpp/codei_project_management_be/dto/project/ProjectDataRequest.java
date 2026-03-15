package dpp.codei_project_management_be.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDataRequest {

    private String projectName;
    private String branch;
    private String notes;
    private List<String> taskManagements = new ArrayList<>();
    private List<String> repositories = new ArrayList<>();
    private List<String> pics = new ArrayList<>();
    private List<String> devWhiteList = new ArrayList<>();


    public void setTaskManagements(List<String> taskManagements) {
        this.taskManagements = Objects.requireNonNullElseGet(taskManagements, ArrayList::new);
    }


    public void setRepositories(List<String> repositories) {
        this.repositories = Objects.requireNonNullElseGet(repositories, ArrayList::new);
    }


    public void setPics(List<String> pics) {
        this.pics = Objects.requireNonNullElseGet(pics, ArrayList::new);
    }


    public void setDevWhiteList(List<String> devWhiteList) {
        this.devWhiteList = Objects.requireNonNullElseGet(devWhiteList, ArrayList::new);
    }
}


