package ca.bc.gov.educ.api.assessment.service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentAlgorithmData;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentList;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepo;  

    @Autowired
    private AssessmentTransformer assessmentTransformer;
    
    @Autowired
    private StudentAssessmentService studentAssessmentService;
    
    @Autowired
    private AssessmentRequirementService assessmentRequirementService;

    private static Logger logger = LoggerFactory.getLogger(AssessmentService.class);

     /**
     * Get all courses in Course DTO
     *
     * @return Course 
     * @throws java.lang.Exception
     */
    public List<Assessment> getAssessmentList() {
        List<Assessment> assessment  = new ArrayList<>();

        try {
        	assessment = assessmentTransformer.transformToDTO(assessmentRepo.findAll());            
        } catch (Exception e) {
            logger.debug(String.format("Exception: %s",e));
        }

        return assessment;
    }

	public Assessment getAssessmentDetails(String assmtCode) {
		return assessmentTransformer.transformToDTO(assessmentRepo.findByAssessmentCode(assmtCode));
	}

	public AssessmentAlgorithmData getAssessmentAlgorithmData(String pen, String accessToken, boolean sortForUI) {
		AssessmentAlgorithmData assessmentAlgorithmData = new AssessmentAlgorithmData();

        // Student Assessments
        List<StudentAssessment> studentAssessment = studentAssessmentService.getStudentAssessmentList(pen,accessToken, sortForUI);
        assessmentAlgorithmData.setStudentAssessments(studentAssessment);
        if (!studentAssessment.isEmpty()) {
            List<String> assessmentCodes = studentAssessment.stream()
                    .map(StudentAssessment::getAssessmentCode)
                    .distinct()
                    .collect(Collectors.toList());
            AssessmentList assessmentList = new AssessmentList();
            assessmentList.setAssessmentCodes(assessmentCodes);

            // Course Requirements
            AssessmentRequirements assessmentRequirement = assessmentRequirementService.getAssessmentRequirementListByAssessments(assessmentList);
            if (assessmentRequirement != null && !assessmentRequirement.getAssessmentRequirementList().isEmpty()) {
            	assessmentAlgorithmData.setAssessmentRequirements(assessmentRequirement.getAssessmentRequirementList());
            }
            
            //Assessments
            List<Assessment> assessment = getAssessmentList();
            if (!assessment.isEmpty()) {
            	assessmentAlgorithmData.setAssessments(assessment);
            }
        }
        return assessmentAlgorithmData;
    }
}
