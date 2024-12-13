package ca.bc.gov.educ.api.assessment.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class School extends BaseDTO{

	private String schoolId;
	private String districtId;
	private String mincode;
	private String independentAuthorityId;
	private String schoolNumber;
	private String faxNumber;
	private String phoneNumber;
	private String email;
	private String website;
	private String displayName;
	private String displayNameNoSpecialChars;
	private String schoolReportingRequirementCode;
	private String schoolOrganizationCode;
	private String schoolCategoryCode;
	private String facilityTypeCode;
	private String openedDate;
	private String closedDate;
	private boolean canIssueTranscripts;
	private boolean canIssueCertificates;
    
	@Override
	public String toString() {
		return String.format("School [ schoolId:%s, districtId:%s, mincode:%s, independentAuthorityId:%s, schoolNumber:%s, " +
				"faxNumber:%s, phoneNumber:%s, email:%s, website:%s, displayName:%s, displayNameNoSpecialChars:%s, " +
				"schoolReportingRequirementCode:%s, schoolOrganizationCode:%s, schoolCategoryCode:%s, facilityTypeCode:%s, " +
				"openedDate:%s, closedDate:%s, canIssueTranscripts:%s, canIssueCertificates:%s, createUser:%s, createDate:%s, " +
				"updateUser:%s, updateDate:%s ]",
				schoolId, districtId, mincode, independentAuthorityId, schoolNumber, faxNumber, phoneNumber, email, website,
				displayName, displayNameNoSpecialChars, schoolReportingRequirementCode, schoolOrganizationCode, schoolCategoryCode,
				facilityTypeCode, openedDate, closedDate, canIssueTranscripts, canIssueCertificates, getCreateUser(), getCreateDate(),
				getUpdateUser(), getUpdateDate());
	}

	    
}
