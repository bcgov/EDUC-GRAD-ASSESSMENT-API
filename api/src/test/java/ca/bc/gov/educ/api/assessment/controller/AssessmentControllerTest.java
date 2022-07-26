package ca.bc.gov.educ.api.assessment.controller;

import ca.bc.gov.educ.api.assessment.model.dto.*;
import ca.bc.gov.educ.api.assessment.service.AssessmentService;
import ca.bc.gov.educ.api.assessment.util.GradValidation;
import ca.bc.gov.educ.api.assessment.util.ResponseHelper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class AssessmentControllerTest {
    @Mock
    private AssessmentService assessmentService;

    @Mock
    ResponseHelper responseHelper;

    @Mock
    GradValidation validation;

    @InjectMocks
    private AssessmentController assessmentController;

    @Test
    public void testGetAllAssessments() {
        final List<Assessment> assessmentList = new ArrayList<>();
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");
        assessmentList.add(assmt);

        Mockito.when(assessmentService.getAssessmentList()).thenReturn(assessmentList);
        assessmentController.getAllAssessments();
        Mockito.verify(assessmentService).getAssessmentList();

    }

    @Test
    public void testGetAssessmentDetails() {
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");

        Mockito.when(assessmentService.getAssessmentDetails(assmt.getAssessmentCode())).thenReturn(assmt);
        assessmentController.getAssessmentDetails(assmt.getAssessmentCode());
        Mockito.verify(assessmentService).getAssessmentDetails(assmt.getAssessmentCode());
    }

}
