package dpp.codei_project_management_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_information")
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Department department;

    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName;

    @Column(name = "branch", length = 200)
    private String branch;

    @Column(name = "notes", length = 3000)
    private String notes;

    @Column(name = "task_managements", columnDefinition = "TEXT")
    private String taskManagements = "[]";

    @Column(name = "repositories", columnDefinition = "TEXT")
    private String repositories = "[]";

    @Column(name = "pics", columnDefinition = "TEXT")
    private String pics = "[]";

    @Column(name = "dev_white_list", columnDefinition = "TEXT")
    private String devWhiteList = "[]";

    @Column(name = "pm_usernames", columnDefinition = "TEXT")
    private String pmUsernames = "[]";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

