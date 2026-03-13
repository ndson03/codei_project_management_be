package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	boolean existsByPartIdAndDepartmentPicUsername(Long partId, String username);
}

