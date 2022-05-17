package ca.bc.gov.educ.api.assessment.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class BaseModel {
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
}
