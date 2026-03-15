package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.department.CreateDepartmentRequest;
import dpp.codei_project_management_be.dto.department.DepartmentResponse;
import dpp.codei_project_management_be.dto.department.UpdateDepartmentRequest;
import dpp.codei_project_management_be.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/departments")
    public ResponseEntity<DepartmentResponse> createDepartment(@RequestBody CreateDepartmentRequest request) {
        var createdDepartment = adminService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DepartmentResponse.from(createdDepartment));
    }

    @PutMapping("/departments/{deptId}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @PathVariable Long deptId,
            @RequestBody UpdateDepartmentRequest request
    ) {
        var updatedDepartment = adminService.updateDepartment(deptId, request);
        return ResponseEntity.ok(DepartmentResponse.from(updatedDepartment));
    }

    @DeleteMapping("/departments/{deptId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long deptId) {
        adminService.deleteDepartment(deptId);
        return ResponseEntity.noContent().build();
    }
}

