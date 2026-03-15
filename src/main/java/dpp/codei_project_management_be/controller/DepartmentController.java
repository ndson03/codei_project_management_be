package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.department.DepartmentResponse;
import dpp.codei_project_management_be.dto.department.UpdateDepartmentRequest;
import dpp.codei_project_management_be.dto.project.ProjectDataRequest;
import dpp.codei_project_management_be.dto.project.ProjectResponse;
import dpp.codei_project_management_be.entity.Department;
import dpp.codei_project_management_be.entity.Project;
import dpp.codei_project_management_be.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        List<DepartmentResponse> responses = departmentService.getAllDepartments().stream()
                .map(DepartmentResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{deptId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long deptId,
            @RequestBody UpdateDepartmentRequest request
    ) {
        Department updatedDepartment = departmentService.updateDepartment(deptId, request);
        return ResponseEntity.ok(DepartmentResponse.from(updatedDepartment));
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

