package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	boolean existsByIdAndPmsUsername(Long id, String username);
}

