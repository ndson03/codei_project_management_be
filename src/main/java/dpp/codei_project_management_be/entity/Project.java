package dpp.codei_project_management_be.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable(name = "project_task_managements", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "task_management")
    private List<String> taskManagements = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_repositories", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "repository")
    private List<String> repositories = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_pics", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "pic")
    private List<String> pics = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "project_dev_white_list", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "dev_username")
    private List<String> devWhiteList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "project_pm",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> pms = new HashSet<>();
}

