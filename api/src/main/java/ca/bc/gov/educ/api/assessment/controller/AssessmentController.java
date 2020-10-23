package ca.bc.gov.educ.api.assessment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;

@CrossOrigin
@RestController
@RequestMapping(EducAssessmentApiConstants.GRAD_COURSE_API_ROOT_MAPPING)
public class AssessmentController {

    private static Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    @Autowired
    AssessmentService assessmentService;

    @GetMapping
    public List<Assessment> getAllAssessments() { 
    	logger.debug("getAllAssessments : ");
        return assessmentService.getAssessmentList();
    }
}
