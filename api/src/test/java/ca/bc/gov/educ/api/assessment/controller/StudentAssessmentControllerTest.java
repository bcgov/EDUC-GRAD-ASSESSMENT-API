package ca.bc.gov.educ.api.assessment.controller;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.service.StudentAssessmentService;
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
import org.springframework.http.HttpStatus;


import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class StudentAssessmentControllerTest {

    @Mock
    private StudentAssessmentService studentAssessmentService;

    @InjectMocks
    StudentAssessmentController studentAssessmentController;

    @Mock
    ResponseHelper responseHelper;

    @Mock
    GradValidation validation;

    @Test
    public void testGetStudentAssessmentByPEN() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentCode("assmt");
        assessment.setAssessmentName("assmt test");
        assessment.setLanguage("en");

        StudentAssessment studentAssessment = new StudentAssessment();
        studentAssessment.setPen("123456789");
        studentAssessment.setAssessmentCode("assmt");
        studentAssessment.setAssessmentName("assmt test");
        studentAssessment.setAssessmentDetails(assessment);

        Mockito.when(studentAssessmentService.getStudentAssessmentList(studentAssessment.getPen(), "123",true)).thenReturn(Arrays.asList(studentAssessment));
        studentAssessmentController.getStudentAssessmentByPEN(studentAssessment.getPen(), true, "123");
        Mockito.verify(studentAssessmentService).getStudentAssessmentList(studentAssessment.getPen(), "123", true);
    }

    @Test
    public void testGetStudentAssessmentByAssessmentCodeAndPEN() {
        Assessment assessment = new Assessment();
        assessment.setAssessmentCode("assmt");
        assessment.setAssessmentName("assmt test");
        assessment.setLanguage("en");

        StudentAssessment studentAssessment = new StudentAssessment();
        studentAssessment.setPen("123456789");
        studentAssessment.setAssessmentCode("assmt");
        studentAssessment.setAssessmentName("assmt test");
        studentAssessment.setAssessmentDetails(assessment);

        Mockito.when(studentAssessmentService.getStudentAssessment(studentAssessment.getPen(), studentAssessment.getAssessmentCode(), "123",true)).thenReturn(Arrays.asList(studentAssessment));
        studentAssessmentController.getStudentAssessmentByAssessmentCodeAndPEN(studentAssessment.getAssessmentCode(), studentAssessment.getPen(), true, "123");
        Mockito.verify(studentAssessmentService).getStudentAssessment(studentAssessment.getPen(), studentAssessment.getAssessmentCode(), "123", true);
    }

    @Test
    public void testValidationError() {
        Mockito.when(validation.hasErrors()).thenReturn(true);
        var result = studentAssessmentController.getStudentAssessmentByPEN("", true, "");
        Mockito.verify(validation).hasErrors();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testValidationError_whenAssessmentCode_isNotProvided() {
        Mockito.when(validation.hasErrors()).thenReturn(true);
        var result = studentAssessmentController.getStudentAssessmentByAssessmentCodeAndPEN("", "123456789", true, "123");
        Mockito.verify(validation).hasErrors();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
