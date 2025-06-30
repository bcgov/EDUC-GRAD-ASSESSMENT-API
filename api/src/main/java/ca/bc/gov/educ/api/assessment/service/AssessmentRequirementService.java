package ca.bc.gov.educ.api.assessment.service;


import java.util.*;

import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementCodeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ca.bc.gov.educ.api.assessment.model.dto.AllAssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.dto.GradRuleDetails;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentRequirementTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementCodeRepository;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;

@Slf4j
@Service
public class AssessmentRequirementService {

    private final AssessmentRequirementRepository assessmentRequirementRepository;
    private final AssessmentRequirementCodeRepository assessmentRequirementCodeRepository;
    private final AssessmentRequirementTransformer assessmentRequirementTransformer;
    private final EducAssessmentApiConstants constants;
    private final WebClient assessmentApiClient;
    private final AssessmentService assessmentService;
    private final RESTService restService;

    @Autowired
    public AssessmentRequirementService(AssessmentRequirementRepository assessmentRequirementRepository,
                                        AssessmentRequirementCodeRepository assessmentRequirementCodeRepository,
                                        AssessmentRequirementTransformer assessmentRequirementTransformer,
                                        AssessmentService assessmentService,
                                        EducAssessmentApiConstants constants,
                                        @Qualifier("assessmentApiClient") WebClient assessmentApiClient, RESTService restService) {
        this.assessmentRequirementRepository = assessmentRequirementRepository;
        this.assessmentRequirementCodeRepository = assessmentRequirementCodeRepository;
        this.assessmentRequirementTransformer = assessmentRequirementTransformer;
        this.assessmentService = assessmentService;
        this.constants = constants;
        this.assessmentApiClient = assessmentApiClient;
        this.restService = restService;
    }

    /**
     * Get all course requirements in Course Requirement DTO
     *
     * @param pageSize - number of pages to list out assessment requirements //GRAD2-1929 Refactoring/Linting
     * @param pageNo - page number of assessment list
     * @return list - course's assessement requirements list
     */
    public List<AllAssessmentRequirements> getAllAssessmentRequirementList(Integer pageNo, Integer pageSize) {
        List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        List<AllAssessmentRequirements> allAssessmentRequiremntList = new ArrayList<>();
        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findAll(paging);
            assessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent());
            for (AssessmentRequirement cR : assessmentReqList) {
                AllAssessmentRequirements obj = new AllAssessmentRequirements();
                BeanUtils.copyProperties(cR, obj);
                Assessment assmt = assessmentService.getAssessmentDetails(cR.getAssessmentCode());
                obj.setAssessmentName(assmt.getAssessmentName());
                obj.setRuleCode(cR.getRuleCode().getAssmtRequirementCode());
                List<GradRuleDetails> ruleList = restService.get(
                        String.format(constants.getRuleDetailOfProgramManagementApiUrl(), cR.getRuleCode().getAssmtRequirementCode()),
                        new ParameterizedTypeReference<List<GradRuleDetails>>() {
                        }, assessmentApiClient);

                StringBuilder requirementProgram = new StringBuilder();
                requirementProgram = processRuleList(ruleList, requirementProgram, obj);
                obj.setTraxReqNumber(!ruleList.isEmpty() ? ruleList.get(0).getTraxReqNumber() : null);
                obj.setRequirementProgram(requirementProgram.toString());
                allAssessmentRequiremntList.add(obj);
            }
        } catch (Exception e) {
            log.debug(String.format("Exception: %s",e));
        }

        return allAssessmentRequiremntList;
    }

    /**
     * Get all course requirements in Course Requirement DTO by Rule
     *
     * @param pageSize - number of pages to list out assessment requirements //GRAD2-1929 Refactoring/Linting
     * @param pageNo - page number of assessment list
     * @return list - assessement requirements list
     */
    public List<AssessmentRequirement> getAllAssessmentRequirementListByRule(String rule, Integer pageNo, Integer pageSize) {
        List<AssessmentRequirement> assessmentReqList = new ArrayList<>();

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Optional<AssessmentRequirementCodeEntity> assessmentRequirementCodeEntity = assessmentRequirementCodeRepository.findById(rule);
            if(assessmentRequirementCodeEntity.isPresent()) {
                Page<AssessmentRequirementEntity> pagedResult = assessmentRequirementRepository.findByRuleCode(assessmentRequirementCodeEntity.get(), paging);
                assessmentReqList = assessmentRequirementTransformer.transformToDTO(pagedResult.getContent());
                assessmentReqList.forEach(cR -> {
                    Assessment assmt = assessmentService.getAssessmentDetails(cR.getAssessmentCode());
                    if (assmt != null) {
                        cR.setAssessmentName(assmt.getAssessmentName());
                    }
                });
            }
        } catch (Exception e) {
            log.debug(String.format("Exception: %s",e));
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
     * @param assessmentCode - assessment code // GRAD2-1929 Refactoring/Linting
     * @param ruleCode - rule code
     * @return object - assessment requirement
     */
    public AssessmentRequirement createAssessmentRequirement(final String assessmentCode, final String ruleCode) {
        AssessmentRequirementEntity assessmentRequirementEntity = populate(assessmentCode, ruleCode);

        AssessmentRequirementEntity currentEntity = assessmentRequirementRepository.findByAssessmentCodeAndRuleCode(
                assessmentRequirementEntity.getAssessmentCode(), assessmentRequirementEntity.getRuleCode());
        log.debug("Create AssessmentRequirement: assessment [{}], rule [{}]", assessmentCode, ruleCode);
        /*
        Add and Update
        GRAD2 -1929 Refactoring/Linting reducing the lines by using requireNonNullElse
        */
        return assessmentRequirementTransformer.transformToDTO(assessmentRequirementRepository.save(Objects.requireNonNullElse(currentEntity, assessmentRequirementEntity)));
    }

    private AssessmentRequirementEntity populate(String assessmentCode, String assessmentRequirementCode) {
        AssessmentRequirementEntity assessmentRequirement = new AssessmentRequirementEntity();
        assessmentRequirement.setAssessmentCode(assessmentCode);

        Optional<AssessmentRequirementCodeEntity> assessmentCodeRequirementCodeOptional = assessmentRequirementCodeRepository.findById(assessmentRequirementCode);
        assessmentCodeRequirementCodeOptional.ifPresent(assessmentRequirement::setRuleCode); //Grad2-1929 Refactoring/Linting
        assessmentRequirement.setAssessmentRequirementId(UUID.randomUUID());
        return assessmentRequirement;
    }
}
