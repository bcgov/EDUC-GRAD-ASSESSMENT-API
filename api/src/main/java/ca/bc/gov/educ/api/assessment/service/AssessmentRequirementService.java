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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.educ.api.assessment.model.dto.AllAssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.dto.GradRuleDetails;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentRequirementTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;

@Service
public class AssessmentRequirementService {

    @Autowired
    private AssessmentRequirementRepository assessmentRequirementRepository;

    @Autowired
    private AssessmentRequirementTransformer assessmentRequirementTransformer;
    
    @Value(EducAssessmentApiConstants.ENDPOINT_RULE_DETAIL_URL)
    private String getRuleDetails;
    
    @Autowired
    WebClient webClient;
    
    @Autowired
    private AssessmentService assessmentService;

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
        try {  
        	Pageable paging = PageRequest.of(pageNo, pageSize);        	 
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findAll(paging);        	
            AssessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent()); 
            AssessmentReqList.forEach((cR) -> {
            	AllAssessmentRequirements obj = new AllAssessmentRequirements();
            	BeanUtils.copyProperties(cR, obj);
            	Assessment assmt = assessmentService.getAssessmentDetails(cR.getAssessmentCode());
            	obj.setAssessmentName(assmt.getAssessmentName());
            	List<GradRuleDetails> ruleList = webClient.get().uri(String.format(getRuleDetails,cR.getRuleCode())).headers(h -> h.setBearerAuth(accessToken)).retrieve().bodyToMono(new ParameterizedTypeReference<List<GradRuleDetails>>() {}).block();
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
            assessmentReqList.forEach(aR -> {
            	Assessment assmt = assessmentService.getAssessmentDetails(aR.getAssessmentCode());
            	aR.setAssessmentName(assmt.getAssessmentName());
            });
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return assessmentReqList;
    }
}
