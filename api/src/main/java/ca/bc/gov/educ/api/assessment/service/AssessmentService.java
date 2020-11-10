package ca.bc.gov.educ.api.assessment.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepo;  

    @Autowired
    private AssessmentTransformer assessmentTransformer;

    private static Logger logger = LoggerFactory.getLogger(AssessmentService.class);

     /**
     * Get all courses in Course DTO
     *
     * @return Course 
     * @throws java.lang.Exception
     */
    public List<Assessment> getAssessmentList() {
        List<Assessment> assessment  = new ArrayList<Assessment>();

        try {
        	assessment = assessmentTransformer.transformToDTO(assessmentRepo.findAll());            
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return assessment;
    }

	public Assessment getAssessmentDetails(String assmtCode) {
		return assessmentTransformer.transformToDTO(assessmentRepo.findByAssessmentCode(assmtCode));
	}
}
