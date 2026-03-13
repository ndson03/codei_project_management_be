package dpp.codei_project_management_be.dto.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectUpdateInfoRequest {

    private String branch;
    private List<String> repositories = new ArrayList<>();

    public ProjectUpdateInfoRequest() {
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = Objects.requireNonNullElseGet(repositories, ArrayList::new);
    }
}


