package ca.bc.gov.educ.api.assessment.model.dto;

import java.sql.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class AssessmentRequirement {

	private UUID assessmentRequirementId;
	private String assmCode;
    private String courseLevel;
    private String ruleCode;
    private String createdBy;
	private Date createdTimestamp;
	private String updatedBy;	
	private Date updatedTimestamp;
	
	@Override
	public String toString() {
		return "AssessmentRequirement [assessmentRequirementId=" + assessmentRequirementId + ", assmCode=" + assmCode
				+ ", courseLevel=" + courseLevel + ", ruleCode=" + ruleCode + ", createdBy=" + createdBy
				+ ", createdTimestamp=" + createdTimestamp + ", updatedBy=" + updatedBy + ", updatedTimestamp="
				+ updatedTimestamp + "]";
	}	
}
