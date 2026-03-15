package dpp.codei_project_management_be.dto.statistic;

import dpp.codei_project_management_be.entity.StatisticResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StatisticResultResponse {

    private Long departmentId;
    private String departmentName;
    private String project;
    private String issueKey;
    private Double prNumber;
    private LocalDateTime createdTime;
    private LocalDateTime mergedTime;
    private String week;
    private String aiSupport;
    private Double numberOfCommit;
    private Double numberOfSegments;
    private String pattern;
    private Double numberOfFile;
    private Double aiLoc;
    private Double firstAiLoc;
    private Double developerLoc;
    private Double aiContribution;
    private String service;
    private String language;
    private String taskType;
    private String devType;
    private Double cycleTimeHour;

    public static StatisticResultResponse from(StatisticResult result) {
        StatisticResultResponse response = new StatisticResultResponse();
        response.setDepartmentId(result.getDepartmentId());
        response.setDepartmentName(result.getDepartmentName());
        response.setProject(result.getProjectName());
        response.setIssueKey(result.getIssueKey());
        response.setPrNumber(result.getPrNumber());
        response.setCreatedTime(result.getCreatedTime());
        response.setMergedTime(result.getMergedTime());
        response.setWeek(result.getWeek());
        response.setAiSupport(result.getAiSupport());
        response.setNumberOfCommit(result.getNumberOfCommit());
        response.setNumberOfSegments(result.getNumberOfSegments());
        response.setPattern(result.getPattern());
        response.setNumberOfFile(result.getNumberOfFile());
        response.setAiLoc(result.getAiLoc());
        response.setFirstAiLoc(result.getFirstAiLoc());
        response.setDeveloperLoc(result.getDeveloperLoc());
        response.setAiContribution(result.getAiContribution());
        response.setService(result.getService());
        response.setLanguage(result.getLanguage());
        response.setTaskType(result.getTaskType());
        response.setDevType(result.getDevType());
        response.setCycleTimeHour(result.getCycleTimeHour());
        return response;
    }
}
