package ca.bc.gov.educ.api.assessment.util;

public class PermissionsConstants {

	private PermissionsConstants() {}

	public static final String _PREFIX = "hasAuthority('";
	public static final String _SUFFIX = "')";

	public static final String READ_GRAD_ASSESSMENT = _PREFIX + "SCOPE_READ_GRAD_ASSESSMENT_DATA" + _SUFFIX;
	public static final String READ_GRAD_ASSESSMENT_REQUIREMENT = _PREFIX + "SCOPE_READ_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
	public static final String CREATE_GRAD_ASSESSMENT_REQUIREMENT = _PREFIX + "SCOPE_CREATE_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
	public static final String READ_ALGORITHM_DATA = _PREFIX + "SCOPE_READ_GRAD_ASSESSMENT_DATA" + _SUFFIX
		+ " and " + _PREFIX + "SCOPE_READ_GRAD_ASSESSMENT_REQUIREMENT_DATA" + _SUFFIX;
}
