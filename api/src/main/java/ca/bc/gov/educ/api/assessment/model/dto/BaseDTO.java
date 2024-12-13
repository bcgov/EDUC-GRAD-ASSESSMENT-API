package ca.bc.gov.educ.api.assessment.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BaseDTO {
	private String createUser;
	private String createDate;
	@NotBlank(message = "updateUser must not be null or empty")
	private String updateUser;
	private String updateDate;
}
