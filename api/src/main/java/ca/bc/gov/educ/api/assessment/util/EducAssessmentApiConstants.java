package ca.bc.gov.educ.api.assessment.util;

import java.util.Date;

public class EducAssessmentApiConstants {

    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String GRAD_ASSESSMENT_API_ROOT_MAPPING = "/api/" + API_VERSION + "/assessment";
    public static final String GET_ASSESSMENT_BY_CODE_MAPPING = "/{assmtCode}";
    public static final String GET_ASSESSMENT_REQUIREMENT_MAPPING = "/requirement";
    public static final String GET_ASSESSMENT_REQUIREMENT_BY_RULE_MAPPING = "/requirement/rule";
    public static final String GET_ASSESSMENT_REQUIREMENT_BY_ASSESSMENT_LIST_MAPPING="/requirement/assessment-list";

    public static final String ENDPOINT_RULE_DETAIL_URL="${endpoint.programmanagement-api.rule-detail.url}";
    //Attribute Constants
    public static final String STUDENT_COURSE_ID_ATTRIBUTE = "courseID";

    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "AssessmentAPI";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "AssessmentAPI";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
    
    public static final String TRAX_DATE_FORMAT = "yyyyMM";
}
