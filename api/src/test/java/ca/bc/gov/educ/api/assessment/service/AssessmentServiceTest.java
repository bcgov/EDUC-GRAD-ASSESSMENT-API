package ca.bc.gov.educ.api.assessment.service;

import ca.bc.gov.educ.api.assessment.model.dto.*;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssessmentServiceTest {

    @Autowired
    EducAssessmentApiConstants constants;

    @Autowired
    private AssessmentService assessmentService;

    @MockBean
    private AssessmentRepository assessmentRepository;

    @MockBean
    private StudentAssessmentService studentAssessmentService;

    @MockBean
    private AssessmentRequirementService assessmentRequirementService;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetAssessmentList() {
        final List<AssessmentEntity> assessmentList = new ArrayList<>();
        final AssessmentEntity assmt = new AssessmentEntity();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");
        assessmentList.add(assmt);

        when(assessmentRepository.findAll()).thenReturn(assessmentList);
        var result = assessmentService.getAssessmentList();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testGetAssessmentDetails() {
        final AssessmentEntity assmt = new AssessmentEntity();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");

        Optional<AssessmentEntity> optional = Optional.of(assmt);

        when(assessmentRepository.findByAssessmentCode(assmt.getAssessmentCode())).thenReturn(optional);
        var result = assessmentService.getAssessmentDetails(assmt.getAssessmentCode());
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentCode()).isEqualTo(assmt.getAssessmentCode());
    }

    @Test
    public void testGetAssessmentAlgorithmData() {
        String pen = "23232323";
        String accessToken = "accessToken";
        List<StudentAssessment> sList = new ArrayList<>();
        StudentAssessment studentAssessment = new StudentAssessment();
        studentAssessment.setAssessmentCode("LTE12");
        studentAssessment.setSpecialCase("special");
        studentAssessment.setMincodeAssessment("12345678");
        sList.add(studentAssessment);

        AssessmentList assessmentList = new AssessmentList();
        List<String> aList = new ArrayList<>();
        aList.add("LTE12");
        assessmentList.setAssessmentCodes(aList);

        AssessmentRequirement ar = new AssessmentRequirement();
        ar.setAssessmentName("sdfds");
        ar.setAssessmentRequirementId(new UUID(1,1));
        AssessmentRequirementCode code = new AssessmentRequirementCode();
        code.setAssmtRequirementCode("100");
        ar.setRuleCode(code);
        List<AssessmentRequirement>list =new ArrayList<>();
        list.add(ar);
        AssessmentRequirements ars = new AssessmentRequirements();
        ars.setAssessmentRequirementList(list);

        final List<AssessmentEntity> assessmentLists = new ArrayList<>();
        final AssessmentEntity assmt = new AssessmentEntity();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");
        assessmentLists.add(assmt);


        when(studentAssessmentService.getStudentAssessmentList(pen,accessToken, true)).thenReturn(sList);
        when(assessmentRepository.findAll()).thenReturn(assessmentLists);
        var result = assessmentService.getAssessmentAlgorithmData(pen,accessToken,true);
        assertThat(result).isNotNull();
        assertThat(result.getAssessments().size()).isEqualTo(1);
    }
}
