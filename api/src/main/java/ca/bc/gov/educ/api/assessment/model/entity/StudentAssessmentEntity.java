package ca.bc.gov.educ.api.assessment.model.entity;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "TRAX_STUDENT_ASSESSMENTS")
public class StudentAssessmentEntity {
	@EmbeddedId
    private StudentAssessmentId assessmentKey;   
    
    @Column(name = "SPECIAL_CASE", nullable = true)
    private String specialCase;

    @Column(name = "EXCEEDED_WRITES_FLAG", nullable = true)
    private String exceededWriteFlag;
    
    @Column(name = "ASSM_PROFICIENCY_SCORE", nullable = true)
    private Double proficiencyScore;

    @Column(name = "WROTE_FLAG", nullable = true)
    private String wroteFlag;
    
    @Column(name = "MINCODE_ASSMT", nullable = true)
    private String mincodeAssessment;
    
    
  
}
