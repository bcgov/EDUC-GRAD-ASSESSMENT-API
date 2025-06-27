package ca.bc.gov.educ.api.assessment.controller;

import ca.bc.gov.educ.api.assessment.model.dto.*;
import ca.bc.gov.educ.api.assessment.service.AssessmentRequirementService;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.*;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_ASSESSMENT_API_ROOT_MAPPING)
@OpenAPIDefinition(info = @Info(title = "API for Assessment Requirement Management.", description = "This API is for Assessment Requirement Management.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_ASSESSMENT_REQUIREMENT_DATA"})})
public class AssessmentRequirementController {

    AssessmentRequirementService assessmentRequirementService;
    AssessmentService assessmentService;
    GradValidation validation;
    ResponseHelper response;

    @Autowired
    public AssessmentRequirementController(AssessmentRequirementService assessmentRequirementService,
                                           AssessmentService assessmentService,
                                           GradValidation validation,
                                           ResponseHelper response) {
        this.assessmentRequirementService = assessmentRequirementService;
        this.assessmentService = assessmentService;
        this.validation = validation;
        this.response = response;
    }

    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_MAPPING)
    @PreAuthorize(PermissionsConstants.READ_GRAD_ASSESSMENT_REQUIREMENT)
    @Operation(summary = "Find All Assessment Requirements", description = "Get All Assessment Requirements",
            tags = {"Assessment Requirements"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<AllAssessmentRequirements>> getAllAssessmentRequirement(
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "150") Integer pageSize) {
        log.debug("getAllAssessmentRequirement : ");
        return response.GET(assessmentRequirementService.getAllAssessmentRequirementList(pageNo, pageSize));
    }

    @PostMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_MAPPING)
    @PreAuthorize(PermissionsConstants.CREATE_GRAD_ASSESSMENT_REQUIREMENT)
    @Operation(summary = "Create an Assessment Requirement", description = "Create an Assessment Requirement",
            tags = {"Assessment Requirements"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST")})
    public ResponseEntity<ApiResponseModel<AssessmentRequirement>> createAssessmentRequirement(@RequestBody AssessmentRequirement assessmentRequirement) {
        log.debug("createAssessmentRequirement : ");
        validation.requiredField(assessmentRequirement.getAssessmentCode(), "Assessment Code");
        if (assessmentRequirement.getRuleCode() == null) {
            validation.addError("Rule Code Entity is required.");
        } else {
            validation.requiredField(assessmentRequirement.getRuleCode().getAssmtRequirementCode(), "Rule Code");
        }
        if (validation.hasErrors()) {
            validation.stopOnErrors();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return response.CREATED(assessmentRequirementService.createAssessmentRequirement(assessmentRequirement.getAssessmentCode(), assessmentRequirement.getRuleCode().getAssmtRequirementCode()));
    }

    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_BY_RULE_MAPPING)
    @PreAuthorize(PermissionsConstants.READ_GRAD_ASSESSMENT_REQUIREMENT)
    @Operation(summary = "Find All Assessment Requirements by Rule", description = "Get All Assessment Requirements by Rule",
            tags = {"Assessment Requirements"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<AssessmentRequirement>> getAllAssessmentRequirementByRule(
            @RequestParam(value = "rule", required = true) String rule,
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "150") Integer pageSize) {
        log.debug("getAllAssessmentRequirementByRule : ");
        return response.GET(assessmentRequirementService.getAllAssessmentRequirementListByRule(rule, pageNo, pageSize));
    }

    @PostMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_BY_ASSESSMENT_LIST_MAPPING)
    @PreAuthorize(PermissionsConstants.READ_GRAD_ASSESSMENT_REQUIREMENT)
    @Operation(summary = "Find all Assessment Requirements by Assessment Code list",
            description = "Get all Assessment Requirements by Assessment Code list", tags = {"Assessment Requirements"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<AssessmentRequirements> getAssessmentRequirementByAssessments(@RequestBody AssessmentList assessmentList) {
        log.debug("getAssessmentRequirementByAssessments : ");
        return response.GET(assessmentService.getAssessmentRequirementListByAssessments(assessmentList));
    }

}
