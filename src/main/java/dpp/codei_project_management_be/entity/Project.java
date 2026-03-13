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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project_information")
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

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getTaskManagements() {
        return taskManagements;
    }

    public void setTaskManagements(List<String> taskManagements) {
        this.taskManagements = taskManagements;
    }

    public List<String> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<String> repositories) {
        this.repositories = repositories;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<String> getDevWhiteList() {
        return devWhiteList;
    }

    public void setDevWhiteList(List<String> devWhiteList) {
        this.devWhiteList = devWhiteList;
    }

    public Set<User> getPms() {
        return pms;
    }

    public void setPms(Set<User> pms) {
        this.pms = pms;
    }
}

