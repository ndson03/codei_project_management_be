package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.dto.project.ProjectResponse;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/{deptId}/projects")
    public ResponseEntity<ProjectResponse> createProject(
            @PathVariable Long deptId,
            @RequestBody ProjectDataRequest request
    ) {
        Project createdProject = departmentService.createProject(deptId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.from(createdProject));
    }
}

