package ca.bc.gov.educ.api.assessment.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
public class EducAssessmentApiConstants {

    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String GRAD_ASSESSMENT_API_ROOT_MAPPING = "/api/" + API_VERSION + "/assessment";
    public static final String GET_ASSESSMENT_BY_CODE_MAPPING = "/{assmtCode}";
    public static final String GET_ASSESSMENT_REQUIREMENT_MAPPING = "/requirement";
    public static final String GET_ASSESSMENT_REQUIREMENT_BY_RULE_MAPPING = "/requirement/rule";
    public static final String GET_ASSESSMENT_REQUIREMENT_BY_ASSESSMENT_LIST_MAPPING="/requirement/assessment-list";
    public static final String GET_STUDENT_ASSESSMENT_BY_ID_MAPPING = "/{studentAssessmentId}";
    public static final String GET_STUDENT_ASSESSMENT_BY_PEN_MAPPING = "/pen/{pen}";
    public static final String GET_ASSESSMENT_ALGORITHM_DATA_BY_PEN_MAPPING = "/assessment-algorithm/pen/{pen}";


    //Attribute Constants
    public static final String STUDENT_ASSESSMENT_ID_ATTRIBUTE = "studentAssessmentID";
    public static final String STUDENT_COURSE_ID_ATTRIBUTE = "courseID";

    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "AssessmentAPI";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "AssessmentAPI";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
    
    public static final String TRAX_DATE_FORMAT = "yyyyMM";

    //Endpoints
    @Value("${endpoint.grad-program-api.rule-detail.url}")
    private String ruleDetailOfProgramManagementApiUrl;

    @Value("${endpoint.grad-trax-api.school-name-by-mincode.url}")
    private String schoolNameByMincodeUrl;

}
