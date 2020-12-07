package ca.bc.gov.educ.api.assessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.service.AssessmentRequirementService;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_ASSESSMENT_API_ROOT_MAPPING)
@EnableResourceServer
@OpenAPIDefinition(info = @Info(title = "API for Assessment Data.", description = "This Read API is for Reading Assessment data.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_ASSESSMENT_DATA","READ_GRAD_ASSESSMENT_REQUIREMENT_DATA"})})
public class AssessmentController {

    private static Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    AssessmentService assessmentService;

    @Autowired
    AssessmentRequirementService assessmentRequirementService;
    
    @GetMapping
    @PreAuthorize("#oauth2.hasScope('READ_GRAD_ASSESSMENT_DATA')")
    public List<Assessment> getAllAssessments() { 
    	logger.debug("getAllAssessments : ");
        return assessmentService.getAssessmentList();
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_BY_CODE_MAPPING)
    @PreAuthorize("#oauth2.hasScope('READ_GRAD_ASSESSMENT_DATA')")
    public Assessment getAssessmentDetails(@PathVariable String assmtCode) { 
    	logger.debug("getAssessmentDetails : ");
        return assessmentService.getAssessmentDetails(assmtCode);
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_MAPPING)
    @PreAuthorize("#oauth2.hasScope('READ_GRAD_ASSESSMENT_REQUIREMENT_DATA')")
    public List<AssessmentRequirement> getAllAssessmentRequirement(
    		@RequestParam(value = "pageNo", required = false,defaultValue = "0") Integer pageNo, 
            @RequestParam(value = "pageSize", required = false,defaultValue = "150") Integer pageSize) { 
    	logger.debug("getAllAssessmentRequirement : ");
        return assessmentRequirementService.getAllAssessmentRequirementList(pageNo,pageSize);
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_BY_RULE_MAPPING)
    @PreAuthorize("#oauth2.hasScope('READ_GRAD_ASSESSMENT_REQUIREMENT_DATA')")
    public List<AssessmentRequirement> getAllAssessmentRequirementByRule(
    		@RequestParam(value = "rule", required = true) String rule,
    		@RequestParam(value = "pageNo", required = false,defaultValue = "0") Integer pageNo, 
            @RequestParam(value = "pageSize", required = false,defaultValue = "150") Integer pageSize) { 
    	logger.debug("getAllAssessmentRequirementByRule : ");
        return assessmentRequirementService.getAllAssessmentRequirementListByRule(rule, pageNo, pageSize);
    }
}
