package ca.bc.gov.educ.api.assessment.service;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentTransformer;
import ca.bc.gov.educ.api.assessment.model.transformer.StudentAssessmentTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;
import ca.bc.gov.educ.api.assessment.repository.StudentAssessmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class StudentAssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentAssessmentService.class);

    StudentAssessmentRepository studentAssessmentRepo;
    AssessmentRepository assessmentRepo;
    StudentAssessmentTransformer studentAssessmentTransformer;
    AssessmentTransformer assessmentTransformer;

    @Autowired
    public StudentAssessmentService(StudentAssessmentRepository studentAssessmentRepo,
                                    AssessmentRepository assessmentRepo,
                                    StudentAssessmentTransformer studentAssessmentTransformer,
                                    AssessmentTransformer assessmentTransformer) {
        this.studentAssessmentRepo = studentAssessmentRepo;
        this.assessmentRepo = assessmentRepo;
        this.studentAssessmentTransformer = studentAssessmentTransformer;
        this.assessmentTransformer = assessmentTransformer;
    }

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
            populateFields(studentAssessment);
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

    public List<StudentAssessment> getStudentAssessment(String pen, String assessmentCode, boolean sortForUI) {
        List<StudentAssessment> studentAssessment = new ArrayList<>();
        try {
            studentAssessment = studentAssessmentTransformer.transformToDTO(studentAssessmentRepo.findByAssessmentKeyPenAndAssessmentKeyAssessmentCode(pen, assessmentCode));
            populateFields(studentAssessment);
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

    private void populateFields(List<StudentAssessment> studentAssessmentList) {
        studentAssessmentList.forEach(sA -> {
            Assessment assessment = assessmentTransformer.transformToDTO(assessmentRepo.findByAssessmentCode(sA.getAssessmentCode()));
            if (assessment != null) {
                sA.setAssessmentName(assessment.getAssessmentName());
                sA.setAssessmentDetails(assessment);
            }
        });
    }
}
