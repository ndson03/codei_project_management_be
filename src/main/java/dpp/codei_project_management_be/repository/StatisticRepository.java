package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.StatisticResult;
import dpp.codei_project_management_be.entity.StatisticResultId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatisticRepository extends JpaRepository<StatisticResult, StatisticResultId> {
	List<StatisticResult> findAllByDepartmentId(Long departmentId);
}

