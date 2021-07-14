package ca.bc.gov.educ.api.assessment.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
public class StudentAssessmentApiConstants {

    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String STUDENT_ASSESSMENT_API_ROOT_MAPPING = "/api/" + API_VERSION + "/studentassessment";
    public static final String GET_STUDENT_ASSESSMENT_BY_ID_MAPPING = "/{studentAssessmentId}";
    public static final String GET_STUDENT_ASSESSMENT_BY_PEN_MAPPING = "/pen/{pen}";

    //Attribute Constants
    public static final String STUDENT_ASSESSMENT_ID_ATTRIBUTE = "studentAssessmentID";

    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "StudentAssessmentAPI";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "StudentAssessmentAPI";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
	public static final String TRAX_DATE_FORMAT = "yyyyMM";

	//Endpoints
	@Value("${endpoint.assessment-api.assessment_by_assmt_code}")
	private String assessmentByAssessmentCodeUrl;

    @Value("${endpoint.school-api.school-name-by-mincode.url}")
    private String schoolNameByMincodeUrl;
}
