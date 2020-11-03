package ca.bc.gov.educ.api.assessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.service.AssessmentRequirementService;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;

@CrossOrigin
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_ASSESSMENT_API_ROOT_MAPPING)
public class AssessmentController {

    private static Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    AssessmentService assessmentService;

    @Autowired
    AssessmentRequirementService assessmentRequirementService;
    
    @GetMapping
    public List<Assessment> getAllAssessments() { 
    	logger.debug("getAllAssessments : ");
        return assessmentService.getAssessmentList();
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_MAPPING)
    public List<AssessmentRequirement> getAllAssessmentRequirement(
    		@RequestParam(value = "pageNo", required = false,defaultValue = "0") Integer pageNo, 
            @RequestParam(value = "pageSize", required = false,defaultValue = "150") Integer pageSize) { 
    	logger.debug("getAllAssessmentRequirement : ");
        return assessmentRequirementService.getAllAssessmentRequirementList(pageNo,pageSize);
    }
    
    @GetMapping(EducAssessmentApiConstants.GET_ASSESSMENT_REQUIREMENT_BY_RULE_MAPPING)
    public List<AssessmentRequirement> getAllAssessmentRequirementByRule(
    		@RequestParam(value = "rule", required = true) String rule,
    		@RequestParam(value = "pageNo", required = false,defaultValue = "0") Integer pageNo, 
            @RequestParam(value = "pageSize", required = false,defaultValue = "150") Integer pageSize) { 
    	logger.debug("getAllAssessmentRequirementByRule : ");
        return assessmentRequirementService.getAllAssessmentRequirementListByRule(rule, pageNo, pageSize);
    }
}
