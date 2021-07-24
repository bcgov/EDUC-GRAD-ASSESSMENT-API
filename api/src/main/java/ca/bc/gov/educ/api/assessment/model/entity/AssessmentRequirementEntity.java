package ca.bc.gov.educ.api.assessment.model.entity;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "ASSESSMENT_REQUIREMENT_CODE", referencedColumnName = "ASSESSMENT_REQUIREMENT_CODE")
    private AssessmentRequirementCodeEntity ruleCode;   

}
