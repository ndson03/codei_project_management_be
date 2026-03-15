package dpp.codei_project_management_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class StatisticResultId implements Serializable {

    @Column(name = "issue_key", nullable = false, length = 100)
    private String issueKey;

    @Column(name = "pr_number", nullable = false)
    private Double prNumber;
}
