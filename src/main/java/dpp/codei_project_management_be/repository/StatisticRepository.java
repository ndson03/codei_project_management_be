package dpp.codei_project_management_be.repository;

import dpp.codei_project_management_be.entity.StatisticResult;
import dpp.codei_project_management_be.entity.StatisticResultId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<StatisticResult, StatisticResultId> {
}

