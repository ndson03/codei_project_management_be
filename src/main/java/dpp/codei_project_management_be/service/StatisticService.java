package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.StatisticResult;
import dpp.codei_project_management_be.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;

    @Transactional(readOnly = true)
    public List<StatisticResult> getAllStatisticResults() {
        return statisticRepository.findAll();
    }
}
