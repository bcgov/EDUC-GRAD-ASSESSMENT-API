package ca.bc.gov.educ.api.assessment.model.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AssessmentList {

	List<String> assessmentCodes;		
}
