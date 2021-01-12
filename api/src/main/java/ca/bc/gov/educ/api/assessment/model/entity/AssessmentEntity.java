package ca.bc.gov.educ.api.assessment.model.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "GRAD_ASSESSMENT")
public class AssessmentEntity {
   
	@Id
	@Column(name = "ASSM_CODE", nullable = true)
    private String assessmentCode;   

    @Column(name = "ASSM_NAME", nullable = true)
    private String assessmentName;   

    @Column(name = "LANGUAGE", nullable = true)
    private String language;
    
    @Column(name = "START_DT", nullable = true)
    private Date startDate;

    @Column(name = "END_DT", nullable = true)
    private Date endDate;
}
