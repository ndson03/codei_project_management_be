package dpp.codei_project_management_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "department_information")
@Getter
@Setter
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "pic_username", referencedColumnName = "username", unique = true)
    private User departmentPic;
}

