package ca.bc.gov.educ.api.assessment.model.entity;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * Embeddable JPA Entity composite primary key consisting of student number,
 * course code, course level and course session. This class is used as an
 * embedded primary key for the entities which do not have a unique primary key
 * defined by the database view they mirror. The Entities must have these named
 * attributes or have mapped attributes which override these names in order to
 * use this class.
 *
 * @author CGI Information Management Consultants Inc.
 */
@Embeddable
@Data
public class StudentAssessmentId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 10)
    @Column(name = "PEN", insertable = false, updatable = false)
    private String pen;
    
    @Column(name = "ASSM_CODE", nullable = true)
    private String assessmentCode;
    
    @Column(name = "ASSM_SESSION", nullable = true)
    private String sessionDate;

    public StudentAssessmentId() {
    }

    /**
     * Constructor method used by JPA to create a composite primary key.
     *
     * @param studNo - student number //GRAD2-1929 Refactoring/Linting
     * @param assessmentCode -assessment code
     * @param assmSession - assessment session
     */
    public StudentAssessmentId(String studNo, String assessmentCode, String assmSession) {
        this.pen = studNo;
        this.assessmentCode = assessmentCode;
        this.sessionDate = assmSession;
    }
}
