package ca.bc.gov.educ.api.assessment.service;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

    @MockBean
    WebClient webClient;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

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
        school.setMinCode("12345678");
        school.setSchoolName("Test School");

        when(studentAssessmentRepo.findByPen(studentAssessmentId.getPen())).thenReturn(Arrays.asList(studentAssessmentEntity));
        when(assessmentRepo.findByAssessmentCode("LTE10")).thenReturn(Optional.of(assessment));

        when(this.webClient.get()).thenReturn(this.requestHeadersUriMock);
        when(this.requestHeadersUriMock.uri(String.format(constants.getSchoolNameByMincodeUrl(), studentAssessmentEntity.getMincodeAssessment()))).thenReturn(this.requestHeadersMock);
        when(this.requestHeadersMock.headers(any(Consumer.class))).thenReturn(this.requestHeadersMock);
        when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
        when(this.responseMock.bodyToMono(School.class)).thenReturn(Mono.just(school));

        var result = studentAssessmentService.getStudentAssessmentList(studentAssessmentId.getPen(), "accessToken", true);
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
        StudentAssessment responseStudentAssessment = result.get(0);
        assertThat(responseStudentAssessment.getAssessmentCode()).isEqualTo(assessment.getAssessmentCode());
        assertThat(responseStudentAssessment.getSpecialCase()).isEqualTo(studentAssessmentEntity.getSpecialCase());
        assertThat(responseStudentAssessment.getMincodeAssessmentName()).isEqualTo(school.getSchoolName());
    }
}
