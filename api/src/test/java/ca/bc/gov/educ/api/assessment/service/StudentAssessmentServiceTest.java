package ca.bc.gov.educ.api.assessment.service;

import ca.bc.gov.educ.api.assessment.model.dto.School;
import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;
import ca.bc.gov.educ.api.assessment.model.entity.StudentAssessmentEntity;
import ca.bc.gov.educ.api.assessment.model.entity.StudentAssessmentId;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;
import ca.bc.gov.educ.api.assessment.repository.StudentAssessmentRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class StudentAssessmentServiceTest {

    @Autowired
    StudentAssessmentService studentAssessmentService;

    @Autowired
    AssessmentService assessmentService;

    @Autowired
    private EducAssessmentApiConstants constants;

    @MockBean
    private StudentAssessmentRepository studentAssessmentRepo;

    @MockBean
    private AssessmentRepository assessmentRepo;

    @Autowired
    AssessmentTransformer assessmentTransformer;

    @Test
    public void testGetStudentAssessmentList() {
        // ID
        StudentAssessmentId studentAssessmentId = new StudentAssessmentId();
        studentAssessmentId.setPen("123456789");
        studentAssessmentId.setAssessmentCode("LTE10");
        studentAssessmentId.setSessionDate("2020-05");

        StudentAssessmentEntity studentAssessmentEntity = new StudentAssessmentEntity();
        studentAssessmentEntity.setAssessmentKey(studentAssessmentId);
        studentAssessmentEntity.setSpecialCase("special");
        studentAssessmentEntity.setMincodeAssessment("12345678");

        AssessmentEntity assessment = new AssessmentEntity();
        assessment.setAssessmentCode("LTE10");
        assessment.setAssessmentName("asdas");

        School school = new School();
        school.setMincode("12345678");
        school.setDisplayName("Test School");

        when(studentAssessmentRepo.findByPen(studentAssessmentId.getPen())).thenReturn(List.of(studentAssessmentEntity));
        when(assessmentRepo.findByAssessmentCode("LTE10")).thenReturn(Optional.of(assessment));

        var result = studentAssessmentService.getStudentAssessmentList(studentAssessmentId.getPen(), "accessToken", true);
        //GRAD2 - 1929 - Refactoring/Linting - Used isNotEmpty() instead of isEmpty().isFalse(), and chained isNotNull() and isNotEmpty()
        assertThat(result).isNotNull().isNotEmpty();
        StudentAssessment responseStudentAssessment = result.get(0);
        assertThat(responseStudentAssessment.getAssessmentCode()).isEqualTo(assessment.getAssessmentCode());
        assertThat(responseStudentAssessment.getSpecialCase()).isEqualTo(studentAssessmentEntity.getSpecialCase());
    }

    @Test
    public void testGetStudentAssessment() {
        // ID
        StudentAssessmentId studentAssessmentId = new StudentAssessmentId();
        studentAssessmentId.setPen("123456789");
        studentAssessmentId.setAssessmentCode("LTE10");
        studentAssessmentId.setSessionDate("2020-05");

        StudentAssessmentEntity studentAssessmentEntity = new StudentAssessmentEntity();
        studentAssessmentEntity.setAssessmentKey(studentAssessmentId);
        studentAssessmentEntity.setSpecialCase("special");
        studentAssessmentEntity.setMincodeAssessment("12345678");

        AssessmentEntity assessment = new AssessmentEntity();
        assessment.setAssessmentCode("LTE10");
        assessment.setAssessmentName("asdas");

        School school = new School();
        school.setMincode("12345678");
        school.setDisplayName("Test School");

        when(studentAssessmentRepo.findByAssessmentKeyPenAndAssessmentKeyAssessmentCode(studentAssessmentId.getPen(), studentAssessmentId.getAssessmentCode())).thenReturn(List.of(studentAssessmentEntity));
        when(assessmentRepo.findByAssessmentCode("LTE10")).thenReturn(Optional.of(assessment));

        var result = studentAssessmentService.getStudentAssessment(studentAssessmentId.getPen(), studentAssessmentId.getAssessmentCode(), "accessToken", true);
        //GRAD2 - 1929 - Refactoring/Linting - Used isNotEmpty() instead of isEmpty().isFalse(), and chained isNotNull() and isNotEmpty()
        assertThat(result).isNotNull().isNotEmpty();
        StudentAssessment responseStudentAssessment = result.get(0);
        assertThat(responseStudentAssessment.getAssessmentCode()).isEqualTo(assessment.getAssessmentCode());
        assertThat(responseStudentAssessment.getSpecialCase()).isEqualTo(studentAssessmentEntity.getSpecialCase());
    }
}
