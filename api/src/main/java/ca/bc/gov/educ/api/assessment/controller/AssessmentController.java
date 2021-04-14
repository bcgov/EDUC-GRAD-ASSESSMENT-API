package ca.bc.gov.educ.api.assessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.assessment.model.dto.AllAssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.service.AssessmentRequirementService;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import ca.bc.gov.educ.api.assessment.util.GradValidation;
import ca.bc.gov.educ.api.assessment.util.PermissionsContants;
import ca.bc.gov.educ.api.assessment.util.ResponseHelper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_ASSESSMENT_API_ROOT_MAPPING)
@EnableResourceServer
@OpenAPIDefinition(info = @Info(title = "API for Assessment Management.", description = "This API is for Assessment Management.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_ASSESSMENT_DATA","READ_GRAD_ASSESSMENT_REQUIREMENT_DATA"})})
public class AssessmentController {

    private static Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    AssessmentService assessmentService;

    @Autowired
    AssessmentRequirementService assessmentRequirementService;
    
    @Autowired
	GradValidation validation;
    
    @Autowired
	ResponseHelper response;
    
    @GetMapping
    @PreAuthorize(PermissionsContants.READ_GRAD_ASSESSMENT)
    @Operation(summary = "Find All Assessment", description = "Get all Assessment", tags = { "Assessment" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<Assessment>> getAllAssessments() { 
    	logger.debug("getAllAssessments : ");
        return response.GET(assessmentService.getAssessmentList());
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_BY_CODE_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_ASSESSMENT)
    @Operation(summary = "Find an Assessment Detail by Code", description = "Get an Assessment Detail by Code", tags = { "Assessment" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<Assessment> getAssessmentDetails(@PathVariable String assmtCode) { 
    	logger.debug("getAssessmentDetails : ");
        return response.GET(assessmentService.getAssessmentDetails(assmtCode));
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_ASSESSMENT_REQUIREMENT)
    @Operation(summary = "Find All Assessment Requirements", description = "Get All Assessment Requirements", tags = { "Assessment Requirements" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<AllAssessmentRequirements>> getAllAssessmentRequirement(
    		@RequestParam(value = "pageNo", required = false,defaultValue = "0") Integer pageNo, 
            @RequestParam(value = "pageSize", required = false,defaultValue = "150") Integer pageSize) { 
    	logger.debug("getAllAssessmentRequirement : ");
    	OAuth2AuthenticationDetails auth = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails(); 
    	String accessToken = auth.getTokenValue();
        return response.GET(assessmentRequirementService.getAllAssessmentRequirementList(pageNo,pageSize,accessToken));
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_BY_RULE_MAPPING)
    @PreAuthorize(PermissionsContants.READ_GRAD_ASSESSMENT_REQUIREMENT)
    @Operation(summary = "Find All Assessment Requirements by Rule", description = "Get All Assessment Requirements by Rule", tags = { "Assessment Requirements" })
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
    public ResponseEntity<List<AssessmentRequirement>> getAllAssessmentRequirementByRule(
    		@RequestParam(value = "rule", required = true) String rule,
    		@RequestParam(value = "pageNo", required = false,defaultValue = "0") Integer pageNo, 
            @RequestParam(value = "pageSize", required = false,defaultValue = "150") Integer pageSize) { 
    	logger.debug("getAllAssessmentRequirementByRule : ");
        return response.GET(assessmentRequirementService.getAllAssessmentRequirementListByRule(rule, pageNo, pageSize));
    }
}
