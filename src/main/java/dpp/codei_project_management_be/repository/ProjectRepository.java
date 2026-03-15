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
			  and coalesce(p.pm_user_ids, '[]')::jsonb @> cast(concat('[', cast(:userId as text), ']') as jsonb)
		)
		""", nativeQuery = true)
	boolean existsByIdAndPmUserId(@Param("projectId") Long projectId, @Param("userId") Long userId);

	@Query(value = """
		select exists (
			select 1
			from project_information p
			where coalesce(p.pm_user_ids, '[]')::jsonb @> cast(concat('[', cast(:userId as text), ']') as jsonb)
		)
		""", nativeQuery = true)
	boolean existsByPmUserId(@Param("userId") Long userId);

	@Query(value = """
		select p.*
		from project_information p
		where coalesce(p.pm_user_ids, '[]')::jsonb @> cast(concat('[', cast(:userId as text), ']') as jsonb)
		""", nativeQuery = true)
	List<Project> findAllByPmUserId(@Param("userId") Long userId);
}

