package ca.bc.gov.educ.api.assessment.model.entity;

import java.sql.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Immutable
@Entity
@Table(name = "ASSESSMENT_REQUIREMENT")
public class AssessmentRequirementEntity extends BaseEntity {
   
	@Id
	@Column(name = "ASSESSMENT_REQUIREMENT_ID", nullable = false)
    private UUID assessmentRequirementId;

    @Column(name = "ASSESSMENT_CODE", nullable = false)
    private String assessmentCode;   

    @Column(name = "ASSM_REQUIREMENT_RULE_CODE", nullable = true)
    private String ruleCode;   

}
