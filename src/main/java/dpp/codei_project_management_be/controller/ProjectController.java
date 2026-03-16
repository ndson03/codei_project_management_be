package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.dto.project.ProjectResponse;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.service.DepartmentService;
import dpp.codei_project_management_be.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final DepartmentService departmentService;
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> responses = projectService.getAllProjects().stream()
                .map(ProjectResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProjectData(
            @PathVariable Long projectId,
            @RequestBody ProjectDataRequest request
    ) {
        Project updatedProject = departmentService.updateProjectData(projectId, request);
        return ResponseEntity.ok(ProjectResponse.from(updatedProject));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        departmentService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }
}

