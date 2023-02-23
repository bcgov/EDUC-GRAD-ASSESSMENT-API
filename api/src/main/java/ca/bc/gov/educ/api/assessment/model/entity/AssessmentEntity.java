package ca.bc.gov.educ.api.assessment.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)  //GRAD2-1929 Refactoring/Linting
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
