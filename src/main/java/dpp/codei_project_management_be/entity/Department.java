package dpp.codei_project_management_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "department_information")
public class Department {

    @Id
    @Column(name = "part_id")
    private Long partId;

    @Column(name = "part_name", nullable = false, length = 150)
    private String partName;

    @Column(name = "git_pat")
    private String gitPat;

    @Column(name = "ecode_pat")
    private String ecodePat;

    @Column(name = "gerrit_user_name")
    private String gerritUserName;

    @Column(name = "gerrit_http_password")
    private String gerritHttpPassword;

    @Column(name = "jira_sec_pat")
    private String jiraSecPat;

    @Column(name = "jira_mx_pat")
    private String jiraMxPat;

    @Column(name = "jira_la_pat")
    private String jiraLaPat;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pic_user_id", unique = true)
    private User departmentPic;

    public Department() {
    }

    public Long getPartId() {
        return partId;
    }

    public void setPartId(Long partId) {
        this.partId = partId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getGitPat() {
        return gitPat;
    }

    public void setGitPat(String gitPat) {
        this.gitPat = gitPat;
    }

    public String getEcodePat() {
        return ecodePat;
    }

    public void setEcodePat(String ecodePat) {
        this.ecodePat = ecodePat;
    }

    public String getGerritUserName() {
        return gerritUserName;
    }

    public void setGerritUserName(String gerritUserName) {
        this.gerritUserName = gerritUserName;
    }

    public String getGerritHttpPassword() {
        return gerritHttpPassword;
    }

    public void setGerritHttpPassword(String gerritHttpPassword) {
        this.gerritHttpPassword = gerritHttpPassword;
    }

    public String getJiraSecPat() {
        return jiraSecPat;
    }

    public void setJiraSecPat(String jiraSecPat) {
        this.jiraSecPat = jiraSecPat;
    }

    public String getJiraMxPat() {
        return jiraMxPat;
    }

    public void setJiraMxPat(String jiraMxPat) {
        this.jiraMxPat = jiraMxPat;
    }

    public String getJiraLaPat() {
        return jiraLaPat;
    }

    public void setJiraLaPat(String jiraLaPat) {
        this.jiraLaPat = jiraLaPat;
    }

    public User getDepartmentPic() {
        return departmentPic;
    }

    public void setDepartmentPic(User departmentPic) {
        this.departmentPic = departmentPic;
    }
}

