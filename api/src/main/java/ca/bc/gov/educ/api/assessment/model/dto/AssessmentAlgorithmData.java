package ca.bc.gov.educ.api.assessment.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class AssessmentAlgorithmData {
    List<StudentAssessment> studentAssessments;
    List<AssessmentRequirement> assessmentRequirements;
    List<Assessment> assessments;
}
