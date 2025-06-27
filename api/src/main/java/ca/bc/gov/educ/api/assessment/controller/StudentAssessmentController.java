package ca.bc.gov.educ.api.assessment.controller;

import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.service.StudentAssessmentService;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import ca.bc.gov.educ.api.assessment.util.GradValidation;
import ca.bc.gov.educ.api.assessment.util.ResponseHelper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_ASSESSMENT_API_ROOT_MAPPING)
@OpenAPIDefinition(info = @Info(
        title = "API for Student Assessments.", description = "This API is for Reading Student Assessments data.", version = "1"),
        security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_STUDENT_ASSESSMENT_DATA"})})
public class StudentAssessmentController {

    StudentAssessmentService studentAssessmentService;
	GradValidation validation;
	ResponseHelper response;

    @Autowired
    public StudentAssessmentController(StudentAssessmentService studentAssessmentService,
                                       GradValidation validation,
                                       ResponseHelper response) {
        this.studentAssessmentService = studentAssessmentService;
        this.validation = validation;
        this.response = response;
    }

    @GetMapping(EducAssessmentApiConstants.GET_STUDENT_ASSESSMENT_BY_PEN_MAPPING)
    @PreAuthorize("hasAuthority('SCOPE_READ_GRAD_STUDENT_ASSESSMENT_DATA')")
    @Operation(summary = "Find All Student Assessments by PEN", description = "Get All Student Assessments by PEN", tags = { "Student Assessments" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
                            @ApiResponse(responseCode = "204", description = "NO CONTENT"),
                            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<List<StudentAssessment>> getStudentAssessmentByPEN(
            @PathVariable String pen, @RequestParam(value = "sortForUI",required = false,defaultValue = "false") boolean sortForUI,
            @RequestHeader(name="Authorization") String accessToken) {
        validation.requiredField(pen, "Pen");
        if(validation.hasErrors()) {
        	validation.stopOnErrors();
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            log.debug("#Get All Student Assessments by PEN: {}",pen);
            //GRAD2-1929 Refactoring/Linting replaceAll --> replace
	    	List<StudentAssessment> studentAssessmentList = studentAssessmentService.getStudentAssessmentList(pen,accessToken.replace("Bearer ", ""),sortForUI);
	    	if(studentAssessmentList.isEmpty()) {
	        	return response.NO_CONTENT();
	        }
	    	return response.GET(studentAssessmentList);
        }
    }

    @GetMapping(EducAssessmentApiConstants.GET_STUDENT_ASSESSMENT_BY_PEN_AND_CODE_MAPPING)
    @PreAuthorize("hasAuthority('SCOPE_READ_GRAD_STUDENT_ASSESSMENT_DATA')")
    @Operation(summary = "Find Student Assessments by Assessment Code and PEN", description = "Get Student Assessments by Assessment Code and PEN", tags = { "Student Assessments" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<List<StudentAssessment>> getStudentAssessmentByAssessmentCodeAndPEN(
            @PathVariable String assmtCode, @PathVariable String pen,
            @RequestParam(value = "sortForUI",required = false,defaultValue = "false") boolean sortForUI,
            @RequestHeader(name="Authorization") String accessToken) {
        validation.requiredField(pen, "Pen");
        validation.requiredField(assmtCode, "Assessment Code");
        if(validation.hasErrors()) {
            validation.stopOnErrors();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            log.debug("#Get Student Assessments by AssessmentCode and PEN: {} / {}",assmtCode, pen);
            List<StudentAssessment> studentAssessmentList = studentAssessmentService.getStudentAssessment(pen, assmtCode, accessToken.replace("Bearer ", ""), sortForUI);
            return response.GET(studentAssessmentList);
        }
    }
}
