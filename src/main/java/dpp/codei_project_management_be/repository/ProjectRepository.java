package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	boolean existsByIdAndDepartmentDepartmentPicUsername(Long id, String username);
	List<Project> findAllByDepartmentPartId(Long partId);
	List<Project> findAllByDepartmentDepartmentPicUsername(String username);

	@Query(value = """
		select exists (
			select 1
			from project_information p
			where p.id = :projectId
			  and coalesce(p.pics, '[]')::jsonb @> to_jsonb(ARRAY[:username])
		)
		""", nativeQuery = true)
	boolean existsByIdAndPicUsername(@Param("projectId") Long projectId, @Param("username") String username);

	@Query(value = """
		select exists (
			select 1
			from project_information p
			where coalesce(p.pics, '[]')::jsonb @> to_jsonb(ARRAY[:username])
		)
		""", nativeQuery = true)
	boolean existsByPicUsername(@Param("username") String username);

	@Query(value = """
		select p.*
		from project_information p
		where coalesce(p.pics, '[]')::jsonb @> to_jsonb(ARRAY[:username])
		""", nativeQuery = true)
	List<Project> findAllByPicUsername(@Param("username") String username);
}

