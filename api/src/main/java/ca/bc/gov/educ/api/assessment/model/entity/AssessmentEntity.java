package ca.bc.gov.educ.api.assessment.model.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ASSESSMENT_CODE")
public class AssessmentEntity extends BaseEntity {
   
	@Id
	@Column(name = "ASSESSMENT_CODE", nullable = true)
    private String assessmentCode;   

    @Column(name = "LABEL", nullable = true)
    private String assessmentName;

    @Column(name = "DESCRIPTION", nullable = true)
    private String assessmentDescription;

    @Column(name = "ASSESSMENT_LANGUAGE", nullable = true)
    private String language;
    
    @Column(name = "EFFECTIVE_DATE", nullable = true)
    private Date startDate;

    @Column(name = "EXPIRY_DATE", nullable = true)
    private Date endDate;
}
