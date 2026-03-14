package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.project.AssignProjectPmRequest;
import dpp.codei_project_management_be.dto.project.ProjectResponse;
import dpp.codei_project_management_be.dto.project.ProjectUpdateInfoRequest;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.service.DepartmentService;
import dpp.codei_project_management_be.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final DepartmentService departmentService;
    private final ProjectService projectService;

    public ProjectController(DepartmentService departmentService, ProjectService projectService) {
        this.departmentService = departmentService;
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<ProjectResponse> responses = projectService.getAllProjects().stream()
                .map(ProjectResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{projectId}/pm")
    public ResponseEntity<ProjectResponse> assignProjectPm(
            @PathVariable Long projectId,
            @RequestBody AssignProjectPmRequest request
    ) {
        Project updatedProject = departmentService.assignProjectPm(projectId, request.getUserId());
        return ResponseEntity.ok(ProjectResponse.from(updatedProject));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProjectInfo(
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateInfoRequest request
    ) {
        Project updatedProject = projectService.updateProjectInfo(projectId, request);
        return ResponseEntity.ok(ProjectResponse.from(updatedProject));
    }
}

