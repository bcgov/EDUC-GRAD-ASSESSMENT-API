package ca.bc.gov.educ.api.assessment.model.dto;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class AssessmentRequirement extends BaseModel {

	private UUID assessmentRequirementId;
	private String assessmentCode;   
	private AssessmentRequirementCode ruleCode;
	private String assessmentName;
}
