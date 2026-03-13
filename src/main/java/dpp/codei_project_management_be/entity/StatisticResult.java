package dpp.codei_project_management_be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistic_result")
public class StatisticResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Department ID")
    @Column(name = "department_id")
    private Long departmentId;

    @JsonProperty("Department Name")
    @Column(name = "department_name")
    private String departmentName;

    @JsonProperty("Project")
    @Column(name = "project_name")
    private String projectName;

    @JsonProperty("Issue Key")
    @Column(name = "issue_key")
    private String issueKey;

    @JsonProperty("PR Number")
    @Column(name = "pr_number")
    private Double prNumber;

    @JsonProperty("Created Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @JsonProperty("Merged Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "merged_time")
    private LocalDateTime mergedTime;

    @JsonProperty("Week")
    @Column(name = "week")
    private String week;

    @JsonProperty("AI support")
    @Column(name = "ai_support")
    private String aiSupport;

    @JsonProperty("No. of commit")
    @Column(name = "number_of_commit")
    private Double numberOfCommit;

    @JsonProperty("No. of segments")
    @Column(name = "number_of_segments")
    private Double numberOfSegments;

    @JsonProperty("Pattern")
    @Column(name = "pattern")
    private String pattern;

    @JsonProperty("No. of file")
    @Column(name = "number_of_file")
    private Double numberOfFile;

    @JsonProperty("AI LOC")
    @Column(name = "ai_loc")
    private Double aiLoc;

    @JsonProperty("First AI LOC")
    @Column(name = "first_ai_loc")
    private Double firstAiLoc;

    @JsonProperty("Developer LOC")
    @Column(name = "developer_loc")
    private Double developerLoc;

    @JsonProperty("AI contribution")
    @Column(name = "ai_contribution")
    private Double aiContribution;

    @JsonProperty("Service")
    @Column(name = "service")
    private String service;

    @JsonProperty("Language")
    @Column(name = "language")
    private String language;

    @JsonProperty("Task Type")
    @Column(name = "task_type")
    private String taskType;

    @JsonProperty("Dev Type")
    @Column(name = "dev_type")
    private String devType;

    @JsonProperty("Cycle Time Hour")
    @Column(name = "cycle_time_hour")
    private Double cycleTimeHour;

    public StatisticResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    public Double getPrNumber() {
        return prNumber;
    }

    public void setPrNumber(Double prNumber) {
        this.prNumber = prNumber;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getMergedTime() {
        return mergedTime;
    }

    public void setMergedTime(LocalDateTime mergedTime) {
        this.mergedTime = mergedTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getAiSupport() {
        return aiSupport;
    }

    public void setAiSupport(String aiSupport) {
        this.aiSupport = aiSupport;
    }

    public Double getNumberOfCommit() {
        return numberOfCommit;
    }

    public void setNumberOfCommit(Double numberOfCommit) {
        this.numberOfCommit = numberOfCommit;
    }

    public Double getNumberOfSegments() {
        return numberOfSegments;
    }

    public void setNumberOfSegments(Double numberOfSegments) {
        this.numberOfSegments = numberOfSegments;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Double getNumberOfFile() {
        return numberOfFile;
    }

    public void setNumberOfFile(Double numberOfFile) {
        this.numberOfFile = numberOfFile;
    }

    public Double getAiLoc() {
        return aiLoc;
    }

    public void setAiLoc(Double aiLoc) {
        this.aiLoc = aiLoc;
    }

    public Double getFirstAiLoc() {
        return firstAiLoc;
    }

    public void setFirstAiLoc(Double firstAiLoc) {
        this.firstAiLoc = firstAiLoc;
    }

    public Double getDeveloperLoc() {
        return developerLoc;
    }

    public void setDeveloperLoc(Double developerLoc) {
        this.developerLoc = developerLoc;
    }

    public Double getAiContribution() {
        return aiContribution;
    }

    public void setAiContribution(Double aiContribution) {
        this.aiContribution = aiContribution;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public Double getCycleTimeHour() {
        return cycleTimeHour;
    }

    public void setCycleTimeHour(Double cycleTimeHour) {
        this.cycleTimeHour = cycleTimeHour;
    }
}


