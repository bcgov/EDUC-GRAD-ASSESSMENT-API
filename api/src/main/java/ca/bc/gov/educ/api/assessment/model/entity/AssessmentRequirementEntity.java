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
@Table(name = "GRAD_ASSESSMENT_REQUIREMENT")
public class AssessmentRequirementEntity {
   
	@Id
	@Column(name = "ID", nullable = false)
    private UUID assessmentRequirementId;

    @Column(name = "ASSM_CODE", nullable = false)
    private String assessmentCode;   

    @Column(name = "RULE_CODE", nullable = true)
    private String ruleCode;   
    
    @Column(name = "CREATED_BY", nullable = true)
    private String createdBy;
	
	@Column(name = "CREATED_TIMESTAMP", nullable = true)
    private Date createdTimestamp;
	
	@Column(name = "UPDATED_BY", nullable = true)
    private String updatedBy;
	
	@Column(name = "UPDATED_TIMESTAMP", nullable = true)
    private Date updatedTimestamp;
}
