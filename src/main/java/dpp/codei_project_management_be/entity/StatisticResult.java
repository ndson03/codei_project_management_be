package dpp.codei_project_management_be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistic_result")
@Getter
@Setter
@NoArgsConstructor
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
}


