package ca.bc.gov.educ.api.assessment.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.educ.api.assessment.model.dto.AllAssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.dto.GradRuleDetails;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentRequirementTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiUtils;

@Service
public class AssessmentRequirementService {

    @Autowired
    private AssessmentRequirementRepository assessmentRequirementRepository;

    @Autowired
    private AssessmentRequirementTransformer assessmentRequirementTransformer;
    
    @Value(EducAssessmentApiConstants.ENDPOINT_RULE_DETAIL_URL)
    private String getRuleDetails;
    
    @Autowired
    private RestTemplate restTemplate;

    private static Logger logger = LoggerFactory.getLogger(AssessmentRequirementService.class);

     /**
     * Get all course requirements in Course Requirement DTO
     * @param pageSize 
     * @param pageNo 
     *
     * @return Course 
     * @throws java.lang.Exception
     */
    public List<AllAssessmentRequirements> getAllAssessmentRequirementList(Integer pageNo, Integer pageSize,String accessToken) {
        List<AssessmentRequirement> AssessmentReqList  = new ArrayList<AssessmentRequirement>();
        List<AllAssessmentRequirements> allAssessmentRequiremntList = new ArrayList<AllAssessmentRequirements>();
        HttpHeaders httpHeaders = EducAssessmentApiUtils.getHeaders(accessToken);
        try {  
        	Pageable paging = PageRequest.of(pageNo, pageSize);        	 
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findAll(paging);        	
            AssessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent()); 
            AssessmentReqList.forEach((cR) -> {
            	AllAssessmentRequirements obj = new AllAssessmentRequirements();
            	BeanUtils.copyProperties(cR, obj);
            	List<GradRuleDetails> ruleList = restTemplate.exchange(String.format(getRuleDetails,cR.getRuleCode()), HttpMethod.GET,
    					new HttpEntity<>(httpHeaders), new ParameterizedTypeReference<List<GradRuleDetails>>() {}).getBody();
            	String requirementProgram = "";
            	for(GradRuleDetails rL: ruleList) {
            		obj.setRequirementName(rL.getRequirementName());
            		if(rL.getProgramCode() != null) {
            			if("".equalsIgnoreCase(requirementProgram)) {
            				requirementProgram = rL.getProgramCode();
            			}else {
            				requirementProgram = requirementProgram + "|" + rL.getProgramCode();
            			}
            		}
            		if(rL.getSpecialProgramCode() != null) {
            			if("".equalsIgnoreCase(requirementProgram)) {
            				requirementProgram = requirementProgram + rL.getSpecialProgramCode();
            			}else {
            				requirementProgram = requirementProgram + "|" + rL.getSpecialProgramCode();
            			}
            			
            		}
            	}
            	obj.setRequirementProgram(requirementProgram);
            	allAssessmentRequiremntList.add(obj);
            });
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return allAssessmentRequiremntList;
    }
    
    /**
     * Get all course requirements in Course Requirement DTO by Rule
     * @param pageSize 
     * @param pageNo 
     *
     * @return Course 
     * @throws java.lang.Exception
     */
    public List<AssessmentRequirement> getAllAssessmentRequirementListByRule(String rule,Integer pageNo, Integer pageSize) {
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
