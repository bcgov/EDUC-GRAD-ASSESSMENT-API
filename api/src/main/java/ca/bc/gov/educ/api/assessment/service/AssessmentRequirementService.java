package ca.bc.gov.educ.api.assessment.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentRequirementTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;

@Service
public class AssessmentRequirementService {

    @Autowired
    private AssessmentRequirementRepository assessmentRequirementRepository;

    Iterable<AssessmentRequirementEntity> assessmentRequirementEntityList;

    @Autowired
    private AssessmentRequirementTransformer assessmentRequirementTransformer;

    private static Logger logger = LoggerFactory.getLogger(AssessmentRequirementService.class);

     /**
     * Get all course requirements in Course Requirement DTO
     * @param pageSize 
     * @param pageNo 
     *
     * @return Course 
     * @throws java.lang.Exception
     */
    public List<AssessmentRequirement> getAllCourseRequirementList(Integer pageNo, Integer pageSize) {
        List<AssessmentRequirement> courseReqList  = new ArrayList<AssessmentRequirement>();

        try {  
        	Pageable paging = PageRequest.of(pageNo, pageSize);        	 
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findAll(paging);        	
            courseReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent()); 
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return courseReqList;
    }
    
    /**
     * Get all course requirements in Course Requirement DTO by Rule
     * @param pageSize 
     * @param pageNo 
     *
     * @return Course 
     * @throws java.lang.Exception
     */
    public List<AssessmentRequirement> getAllCourseRequirementListByRule(String rule,Integer pageNo, Integer pageSize) {
        List<AssessmentRequirement> assessmentReqList  = new ArrayList<AssessmentRequirement>();

        try {  
        	Pageable paging = PageRequest.of(pageNo, pageSize);        	 
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findByRuleCode(rule,paging);        	
            assessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent()); 
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return assessmentReqList;
    }
}
