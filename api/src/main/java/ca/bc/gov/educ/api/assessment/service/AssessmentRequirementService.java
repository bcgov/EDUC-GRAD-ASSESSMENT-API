package ca.bc.gov.educ.api.assessment.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementCodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.educ.api.assessment.model.dto.AllAssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentList;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.GradRuleDetails;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentRequirementTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementCodeRepository;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;

@Service
public class AssessmentRequirementService {

    @Autowired
    private AssessmentRequirementRepository assessmentRequirementRepository;
    
    @Autowired
    private AssessmentRequirementCodeRepository assessmentRequirementCodeRepository;

    @Autowired
    private AssessmentRequirementTransformer assessmentRequirementTransformer;



    @Autowired
    private EducAssessmentApiConstants contants;

    @Autowired
    WebClient webClient;

    @Autowired
    private AssessmentService assessmentService;

    private static Logger logger = LoggerFactory.getLogger(AssessmentRequirementService.class);

    /**
     * Get all course requirements in Course Requirement DTO
     *
     * @param pageSize
     * @param pageNo
     * @return Course
     * @throws java.lang.Exception
     */
    public List<AllAssessmentRequirements> getAllAssessmentRequirementList(Integer pageNo, Integer pageSize, String accessToken) {
        List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        List<AllAssessmentRequirements> allAssessmentRequiremntList = new ArrayList<>();
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findAll(paging);
            assessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent());
            assessmentReqList.forEach(cR -> {
                AllAssessmentRequirements obj = new AllAssessmentRequirements();
                BeanUtils.copyProperties(cR, obj);
                Assessment assmt = assessmentService.getAssessmentDetails(cR.getAssessmentCode());
                obj.setAssessmentName(assmt.getAssessmentName());
                obj.setRuleCode(cR.getRuleCode().getAssmtRequirementCode());
                List<GradRuleDetails> ruleList = webClient.get()
                        .uri(String.format(contants.getRuleDetailOfProgramManagementApiUrl(), cR.getRuleCode().getAssmtRequirementCode()))
                        .headers(h -> h.setBearerAuth(accessToken))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<GradRuleDetails>>() {
                        })
                        .block();
                StringBuilder requirementProgram = new StringBuilder();
                requirementProgram = processRuleList(ruleList,requirementProgram,obj);
                obj.setTraxReqNumber(!ruleList.isEmpty()?ruleList.get(0).getTraxReqNumber():null);
                obj.setRequirementProgram(requirementProgram.toString());
                allAssessmentRequiremntList.add(obj);
            });
        } catch (Exception e) {
            logger.debug(String.format("Exception: %s",e));
        }

        return allAssessmentRequiremntList;
    }

    /**
     * Get all course requirements in Course Requirement DTO by Rule
     *
     * @param pageSize
     * @param pageNo
     * @return Course
     * @throws java.lang.Exception
     */
    public List<AssessmentRequirement> getAllAssessmentRequirementListByRule(String rule, Integer pageNo, Integer pageSize) {
        List<AssessmentRequirement> assessmentReqList = new ArrayList<>();

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findByRuleCode(assessmentRequirementCodeRepository.getOne(rule), paging);
            assessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent());
            assessmentReqList.forEach(cR -> {
                Assessment assmt = assessmentService.getAssessmentDetails(cR.getAssessmentCode());
                if (assmt != null) {
                    cR.setAssessmentName(assmt.getAssessmentName());
                }
            });
        } catch (Exception e) {
            logger.debug(String.format("Exception: %s",e));
        }

        return assessmentReqList;
    }
    
    private StringBuilder processRuleList(List<GradRuleDetails> ruleList, StringBuilder requirementProgram, AllAssessmentRequirements obj) {
    	for (GradRuleDetails rL : ruleList) {
            obj.setRequirementName(rL.getRequirementName());
            if (rL.getProgramCode() != null) {
                if ("".equalsIgnoreCase(requirementProgram.toString())) {
                    requirementProgram.append(rL.getProgramCode());
                } else {
                    requirementProgram.append("|").append(rL.getProgramCode());
                }
            }
            if (rL.getOptionalProgramCode() != null) {
                if ("".equalsIgnoreCase(requirementProgram.toString())) {
                    requirementProgram.append(rL.getOptionalProgramCode());
                } else {
                	requirementProgram.append("|").append(rL.getOptionalProgramCode());
                }
            }
        }
    	return requirementProgram;
    }

    /**
     *
     * @param assessmentCode
     * @param ruleCode
     * @return
     */
    public AssessmentRequirement createAssessmentRequirement(final String assessmentCode, final String ruleCode) {
        AssessmentRequirementEntity assessmentRequirementEntity = populate(assessmentCode, ruleCode);

        AssessmentRequirementEntity currentEntity = assessmentRequirementRepository.findByAssessmentCodeAndRuleCode(
                assessmentRequirementEntity.getAssessmentCode(), assessmentRequirementEntity.getRuleCode());
        logger.debug("Create AssessmentRequirement: assessment [{}], rule [{}]", assessmentCode, ruleCode);
        if (currentEntity == null) {
            // Add
            return assessmentRequirementTransformer.transformToDTO(assessmentRequirementRepository.save(assessmentRequirementEntity));
        } else {
            // Update
            return assessmentRequirementTransformer.transformToDTO(assessmentRequirementRepository.save(currentEntity));
        }
    }

    private AssessmentRequirementEntity populate(String assessmentCode, String assessmentRequirementCode) {
        AssessmentRequirementEntity assessmentRequirement = new AssessmentRequirementEntity();
        assessmentRequirement.setAssessmentCode(assessmentCode);

        Optional<AssessmentRequirementCodeEntity> assessmentCodeRequirementCodeOptional = assessmentRequirementCodeRepository.findById(assessmentRequirementCode);
        if (assessmentCodeRequirementCodeOptional.isPresent()) {
            assessmentRequirement.setRuleCode(assessmentCodeRequirementCodeOptional.get());
        }
        assessmentRequirement.setAssessmentRequirementId(UUID.randomUUID());
        return assessmentRequirement;
    }
}
