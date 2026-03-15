package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	boolean existsByIdAndPmsUsername(Long id, String username);
	boolean existsByPmsUsername(String username);
	boolean existsByIdAndDepartmentDepartmentPicUsername(Long id, String username);
	List<Project> findAllByDepartmentPartId(Long partId);
	List<Project> findAllByDepartmentDepartmentPicUsername(String username);
	List<Project> findAllByPmsUsername(String username);
}

