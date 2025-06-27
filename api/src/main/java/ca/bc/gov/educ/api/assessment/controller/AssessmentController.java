package ca.bc.gov.educ.api.assessment.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentAlgorithmData;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import ca.bc.gov.educ.api.assessment.util.GradValidation;
import ca.bc.gov.educ.api.assessment.util.PermissionsConstants;
import ca.bc.gov.educ.api.assessment.util.ResponseHelper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Slf4j
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_ASSESSMENT_API_ROOT_MAPPING)
@OpenAPIDefinition(info = @Info(title = "API for Assessment Management.", description = "This API is for Assessment Management.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_ASSESSMENT_DATA"})})
public class AssessmentController {

    AssessmentService assessmentService;
    GradValidation validation;
    ResponseHelper response;

    @Autowired
    public AssessmentController(AssessmentService assessmentService,
                                GradValidation validation,
                                ResponseHelper response) {
        this.assessmentService = assessmentService;
        this.validation = validation;
        this.response = response;
    }

    @GetMapping
    @PreAuthorize(PermissionsConstants.READ_GRAD_ASSESSMENT)
    @Operation(summary = "Find All Assessment", description = "Get all Assessment", tags = {"Assessment"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        log.debug("getAllAssessments : ");
        return response.GET(assessmentService.getAssessmentList());
    }

    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_BY_CODE_MAPPING)
    @PreAuthorize(PermissionsConstants.READ_GRAD_ASSESSMENT)
    @Operation(summary = "Find an Assessment Detail by Code", description = "Get an Assessment Detail by Code",
            tags = {"Assessment"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<Assessment> getAssessmentDetails(@PathVariable String assmtCode) {
        log.debug("getAssessmentDetails : ");
        return response.GET(assessmentService.getAssessmentDetails(assmtCode));
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_ALGORITHM_DATA_BY_PEN_MAPPING)
    @PreAuthorize(PermissionsConstants.READ_ALGORITHM_DATA)
    @Operation(summary = "Find Assessment Algorithm Data by pen", description = "Get Assessment Algorithm Data by pen", tags = { "Courses" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<AssessmentAlgorithmData> getAssessmentAlgorithmData(
            @PathVariable String pen, @RequestHeader(name="Authorization") String accessToken) {
        log.debug("getAssessmentAlgorithmData : ");
        return response.GET(assessmentService.getAssessmentAlgorithmData(pen, accessToken.replaceAll("Bearer ", ""), false));
    }
}
