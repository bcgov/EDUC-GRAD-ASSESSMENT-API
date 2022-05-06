package ca.bc.gov.educ.api.assessment.model.dto;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class AllAssessmentRequirements extends BaseModel {

	private UUID assessmentRequirementId;
	private String assessmentCode;
	private String assessmentName;
    private String ruleCode;
    private String requirementName;
    private String requirementProgram;
    private String traxReqNumber;
}
