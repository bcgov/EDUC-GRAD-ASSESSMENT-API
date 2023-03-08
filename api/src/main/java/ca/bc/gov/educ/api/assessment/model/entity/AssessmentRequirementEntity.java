package ca.bc.gov.educ.api.assessment.model.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = true) //GRAD2-1929 Refactoring/Linting
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
