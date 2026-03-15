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
public class ProjectUpdateInfoRequest {

    private String branch;
    private List<String> repositories = new ArrayList<>();


    public void setRepositories(List<String> repositories) {
        this.repositories = Objects.requireNonNullElseGet(repositories, ArrayList::new);
    }
}


