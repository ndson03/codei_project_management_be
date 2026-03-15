package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	boolean existsByPartIdAndDepartmentPicUsername(Long partId, String username);
	boolean existsByDepartmentPicUsername(String username);
	boolean existsByDepartmentPicUsernameAndPartIdNot(String username, Long partId);
	List<Department> findAllByDepartmentPicUsername(String username);
}

