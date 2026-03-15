package dpp.codei_project_management_be.controller;

import dpp.codei_project_management_be.dto.statistic.StatisticResultResponse;
import dpp.codei_project_management_be.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping
    public ResponseEntity<List<StatisticResultResponse>> getAllStatisticResults() {
        List<StatisticResultResponse> responses = statisticService.getAllStatisticResults().stream()
                .map(StatisticResultResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
