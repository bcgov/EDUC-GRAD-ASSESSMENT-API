package ca.bc.gov.educ.api.assessment.model.dto;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Component
public class GradRuleDetails {

	private String ruleCode; 
	private String requirementName;
	private String programCode;	
	private String optionalProgramCode;
	private String traxReqNumber;
	
	@Override
	public String toString() {
		return "GradRuleDetails [ruleCode=" + ruleCode + ", requirementName=" + requirementName + ", programCode="
				+ programCode + ", traxReqNumber="+traxReqNumber+"]";
	}
	
}
