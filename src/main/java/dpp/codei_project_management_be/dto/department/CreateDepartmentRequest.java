package dpp.codei_project_management_be.dto.department;

public class CreateDepartmentRequest {

    private Long partId;
    private String partName;
    private String gitPat;
    private String ecodePat;
    private String gerritUserName;
    private String gerritHttpPassword;
    private String jiraSecPat;
    private String jiraMxPat;
    private String jiraLaPat;

    public CreateDepartmentRequest() {
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
}

