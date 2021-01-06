package ca.bc.gov.educ.api.assessment.util;

public interface PermissionsContants {
	String _PREFIX = "#oauth2.hasAnyScope('";
	String _SUFFIX = "')";

	String READ_GRAD_ASSESSMENT = _PREFIX + "READ_GRAD_ASSESSMENT_DATA" + _SUFFIX;
	String READ_GRAD_ASSESSMENT_REQUIREMENT = _PREFIX + "READ_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
}
