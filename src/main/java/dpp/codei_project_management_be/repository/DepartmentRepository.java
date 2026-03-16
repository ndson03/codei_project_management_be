package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	// Repository is now simplified - JSON array checking is done in AccessControlService
}

