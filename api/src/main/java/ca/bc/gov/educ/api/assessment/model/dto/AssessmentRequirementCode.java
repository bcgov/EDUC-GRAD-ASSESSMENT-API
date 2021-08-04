package ca.bc.gov.educ.api.assessment.model.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Component
public class AssessmentRequirementCode extends BaseModel {

	private String assmtRequirementCode;
	private String label;
	private String description;
	private Date effectiveDate;
	private Date expiryDate;
}
