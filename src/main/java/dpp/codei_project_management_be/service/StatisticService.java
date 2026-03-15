package dpp.codei_project_management_be.service;

import dpp.codei_project_management_be.entity.StatisticResult;
import dpp.codei_project_management_be.entity.User;
import dpp.codei_project_management_be.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticRepository statisticRepository;
    private final CurrentUserService currentUserService;
    private final AccessControlService accessControlService;

    @Transactional(readOnly = true)
    public List<StatisticResult> getAllStatisticResults() {
        User currentUser = currentUserService.getCurrentUser();
        if (accessControlService.isAdmin(currentUser)) {
            return statisticRepository.findAll();
        }

        Long partId = currentUser.getPartId();
        if (partId == null) {
            return List.of();
        }

        return statisticRepository.findAllByDepartmentId(partId);
    }
}
