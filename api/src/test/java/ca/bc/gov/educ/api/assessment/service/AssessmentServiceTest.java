package ca.bc.gov.educ.api.assessment.service;


import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentTransformer;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    private AssessmentTransformer assessmentTransformer;

    @Before
    public void setUp() {
        openMocks(this);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetAssessmentList() {
        final List<Assessment> assessmentList = new ArrayList<>();
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");
        assessmentList.add(assmt);

        when(assessmentTransformer.transformToDTO(assessmentRepository.findAll())).thenReturn(assessmentList);
        var result = assessmentService.getAssessmentList();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testGetAssessmentDetails() {
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode("Test");
        assmt.setAssessmentName("Test Name");
        assmt.setStartDate(new Date(System.currentTimeMillis() - 10000L));
        assmt.setEndDate(new Date(System.currentTimeMillis() + 10000L));
        assmt.setLanguage("EN");

        when(assessmentTransformer.transformToDTO(assessmentRepository.findByAssessmentCode(assmt.getAssessmentCode()))).thenReturn(assmt);
        var result = assessmentService.getAssessmentDetails(assmt.getAssessmentCode());
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentCode()).isEqualTo(assmt.getAssessmentCode());
    }
}
