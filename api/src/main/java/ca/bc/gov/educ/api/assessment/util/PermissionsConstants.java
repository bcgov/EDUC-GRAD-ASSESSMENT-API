package ca.bc.gov.educ.api.assessment.util;

public class PermissionsConstants {

	private PermissionsConstants() {}

	public static final String _PREFIX = "#oauth2.hasAnyScope('";
	public static final String _SUFFIX = "')";

	public static final String READ_GRAD_ASSESSMENT = _PREFIX + "READ_GRAD_ASSESSMENT_DATA" + _SUFFIX;
	public static final String READ_GRAD_ASSESSMENT_REQUIREMENT = _PREFIX + "READ_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
	public static final String WRITE_GRAD_ASSESSMENT_REQUIREMENT = _PREFIX + "WRITE_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
	public static final String READ_ALGORITHM_DATA = _PREFIX + "READ_GRAD_ASSESSMENT_DATA', 'READ_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
}
