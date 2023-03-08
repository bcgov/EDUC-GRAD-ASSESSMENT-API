package ca.bc.gov.educ.api.assessment.controller;

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

    /*@Test
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

        Authentication authentication = Mockito.mock(Authentication.class);
        OAuth2AuthenticationDetails details = Mockito.mock(OAuth2AuthenticationDetails.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getDetails()).thenReturn(details);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(studentAssessmentService.getStudentAssessmentList(studentAssessment.getPen(), null,true)).thenReturn(Arrays.asList(studentAssessment));
        studentAssessmentController.getStudentAssessmentByPEN(studentAssessment.getPen(), true);
        Mockito.verify(studentAssessmentService).getStudentAssessmentList(studentAssessment.getPen(), null, true);
    }*/

    @Test
    public void testValidationError() {
        Mockito.when(validation.hasErrors()).thenReturn(true);
        var result = studentAssessmentController.getStudentAssessmentByPEN("", true, "");
        Mockito.verify(validation).hasErrors();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
