package ca.bc.gov.educ.api.assessment.service;


import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.School;
import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentTransformer;
import ca.bc.gov.educ.api.assessment.model.transformer.StudentAssessmentTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;
import ca.bc.gov.educ.api.assessment.repository.StudentAssessmentRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class StudentAssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentAssessmentService.class);

    @Autowired
    StudentAssessmentRepository studentAssessmentRepo;

    @Autowired
    AssessmentRepository assessmentRepo;

    @Autowired
    StudentAssessmentTransformer studentAssessmentTransformer;

    @Autowired
    AssessmentTransformer assessmentTransformer;

    @Autowired
    WebClient webClient;

    @Autowired
    EducAssessmentApiConstants constants;

    /**
     * Get all student assessments by PEN populated in Student Assessment DTO
     *
     * @param pen           PEN #
     * @param accessToken   Access Token
     * @return Student Assessment
     */
    public List<StudentAssessment> getStudentAssessmentList(String pen, String accessToken, boolean sortForUI) {
        List<StudentAssessment> studentAssessment = new ArrayList<>();
        try {
            studentAssessment = studentAssessmentTransformer.transformToDTO(studentAssessmentRepo.findByPen(pen));
            populateFields(studentAssessment, accessToken);
        } catch (Exception e) {
            logger.debug(MessageFormat.format("Exception: {0}",e));
        }
        //Grad2-1929 Refactoring/Linting - replaced Collections with studentAssessment List to sort
        if (sortForUI) {
            studentAssessment.sort(Comparator.comparing(StudentAssessment::getPen)
                    .thenComparing(StudentAssessment::getAssessmentCode)
                    .thenComparing(StudentAssessment::getSessionDate));
        }
        return studentAssessment;
    }

    public List<StudentAssessment> getStudentAssessment(String pen, String assessmentCode, String accessToken, boolean sortForUI) {
        List<StudentAssessment> studentAssessment = new ArrayList<>();
        try {
            studentAssessment = studentAssessmentTransformer.transformToDTO(studentAssessmentRepo.findByAssessmentKeyPenAndAssessmentKeyAssessmentCode(pen, assessmentCode));
            populateFields(studentAssessment, accessToken);
        } catch (Exception e) {
            logger.debug(MessageFormat.format("Exception: {0}",e));
        }
        if (sortForUI) {
            studentAssessment.sort(Comparator.comparing(StudentAssessment::getPen)
                    .thenComparing(StudentAssessment::getAssessmentCode)
                    .thenComparing(StudentAssessment::getSessionDate));
        }
        return studentAssessment;
    }

    private void populateFields(List<StudentAssessment> studentAssessmentList, String accessToken) {
        studentAssessmentList.forEach(sA -> {
            Assessment assessment = assessmentTransformer.transformToDTO(assessmentRepo.findByAssessmentCode(sA.getAssessmentCode()));
            if (assessment != null) {
                sA.setAssessmentName(assessment.getAssessmentName());
                sA.setAssessmentDetails(assessment);
            }

            if (StringUtils.isNotBlank(sA.getMincodeAssessment())) {
                School schObj = webClient.get()
                        .uri(String.format(constants.getSchoolNameByMincodeUrl(), sA.getMincodeAssessment()))
                        .headers(h -> h.setBearerAuth(accessToken))
                        .retrieve()
                        .bodyToMono(School.class)
                        .block();
                if (schObj != null)
                    sA.setMincodeAssessmentName(schObj.getDisplayName());
                sA.setHasMoreInfo(true);
            }
        });
    }
}
