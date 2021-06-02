package ca.bc.gov.educ.api.assessment.service;

import ca.bc.gov.educ.api.assessment.model.dto.*;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.model.transformer.AssessmentRequirementTransformer;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssessmentRequirementServiceTest {

    @Autowired
    EducAssessmentApiConstants constants;

    @Autowired
    private AssessmentRequirementService assessmentRequirementService;

    @MockBean
    private AssessmentService assessmentService;

    @MockBean
    private AssessmentRequirementRepository assessmentRequirementRepository;

    @MockBean
    private AssessmentRequirementTransformer assessmentRequirementTransformer;

    @MockBean
    private WebClient webClient;

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

    @Before
    public void setUp() {
        openMocks(this);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetAllAssessmentRequirementList() {
        // ID
        final UUID assessmentRequirementID = UUID.randomUUID();
        final String assessmentCode = "TEST";
        final String assessmentName = "TEST Name description";
        final String ruleCode = "RULE";
        final String requirementName = "ReqName";
        final String requirementProgram = "ReqProg";

        // Assessment Requirements
        final List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        final AssessmentRequirement assmtReq = new AssessmentRequirement();
        assmtReq.setAssessmentRequirementId(assessmentRequirementID);
        assmtReq.setAssessmentCode(assessmentCode);
        assmtReq.setAssessmentName(assessmentName);
        assmtReq.setRuleCode(ruleCode);
        assessmentReqList.add(assmtReq);

        // Assessment Requirements Entity
        final List<AssessmentRequirementEntity> assessmentReqEntityList = new ArrayList<>();
        final AssessmentRequirementEntity assmtReqEntity = new AssessmentRequirementEntity();
        assmtReqEntity.setAssessmentRequirementId(assessmentRequirementID);
        assmtReqEntity.setAssessmentCode(assessmentCode);
        assmtReqEntity.setRuleCode(ruleCode);
        assessmentReqEntityList.add(assmtReqEntity);

        Pageable paging = PageRequest.of(1, 5);
        Page<AssessmentRequirementEntity> pageResult = new PageImpl<AssessmentRequirementEntity>(assessmentReqEntityList);

        // Assessment
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode(assessmentCode);
        assmt.setAssessmentName(assessmentName);

        // Rule Details
        final List<GradRuleDetails> ruleList = new ArrayList<GradRuleDetails>();
        final GradRuleDetails ruleDetail = new GradRuleDetails();
        ruleDetail.setRuleCode(ruleCode);
        ruleDetail.setRequirementName(requirementName);
        ruleDetail.setProgramCode(requirementProgram);
        ruleList.add(ruleDetail);

        final ParameterizedTypeReference<List<GradRuleDetails>> ruleDetailsResponseType = new ParameterizedTypeReference<List<GradRuleDetails>>() {
        };

        when(assessmentRequirementRepository.findAll(paging)).thenReturn(pageResult);
        when(assessmentRequirementTransformer.transformToDTO(pageResult.getContent())).thenReturn(assessmentReqList);
        when(assessmentService.getAssessmentDetails(assessmentCode)).thenReturn(assmt);

        when(this.webClient.get()).thenReturn(this.requestHeadersUriMock);
        when(this.requestHeadersUriMock.uri(String.format(constants.getRuleDetailOfProgramManagementApiUrl(),ruleCode))).thenReturn(this.requestHeadersMock);
        when(this.requestHeadersMock.headers(any(Consumer.class))).thenReturn(this.requestHeadersMock);
        when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
        when(this.responseMock.bodyToMono(ruleDetailsResponseType)).thenReturn(Mono.just(ruleList));

        var result = assessmentRequirementService.getAllAssessmentRequirementList(1, 5, "accessToken");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        AllAssessmentRequirements aar = result.get(0);
        assertThat(aar.getAssessmentRequirementId()).isEqualTo(assessmentRequirementID);
        assertThat(aar.getAssessmentCode()).isEqualTo(assessmentCode);
        assertThat(aar.getAssessmentName()).isEqualTo(assessmentName);
        assertThat(aar.getRequirementName()).isEqualTo(requirementName);
        assertThat(aar.getRequirementProgram()).isEqualTo(requirementProgram);
    }

    @Test
    public void testGetAllAssessmentRequirementListByRule() {
        // ID
        final UUID assessmentRequirementID = UUID.randomUUID();
        final String assessmentCode = "TEST";
        final String assessmentName = "TEST Name description";
        final String ruleCode = "RULE";

        // Assessment Requirements
        final List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        final AssessmentRequirement assmtReq = new AssessmentRequirement();
        assmtReq.setAssessmentRequirementId(assessmentRequirementID);
        assmtReq.setAssessmentCode(assessmentCode);
        assmtReq.setRuleCode(ruleCode);
        assessmentReqList.add(assmtReq);

        // Assessment Requirements Entity
        final List<AssessmentRequirementEntity> assessmentReqEntityList = new ArrayList<>();
        final AssessmentRequirementEntity assmtReqEntity = new AssessmentRequirementEntity();
        assmtReqEntity.setAssessmentRequirementId(assessmentRequirementID);
        assmtReqEntity.setAssessmentCode(assessmentCode);
        assmtReqEntity.setRuleCode(ruleCode);
        assessmentReqEntityList.add(assmtReqEntity);

        Pageable paging = PageRequest.of(1, 5);
        Page<AssessmentRequirementEntity> pageResult = new PageImpl<AssessmentRequirementEntity>(assessmentReqEntityList);

        // Assessment
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode(assessmentCode);
        assmt.setAssessmentName(assessmentName);

        when(assessmentRequirementRepository.findByRuleCode(ruleCode, paging)).thenReturn(pageResult);
        when(assessmentRequirementTransformer.transformToDTO(pageResult.getContent())).thenReturn(assessmentReqList);
        when(assessmentService.getAssessmentDetails(assessmentCode)).thenReturn(assmt);

        var result = assessmentRequirementService.getAllAssessmentRequirementListByRule(ruleCode, 1, 5);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        AssessmentRequirement ar = result.get(0);
        assertThat(ar.getAssessmentCode()).isEqualTo(assessmentCode);
        assertThat(ar.getAssessmentName()).isEqualTo(assessmentName);
    }

    @Test
    public void testGetAssessmentRequirementListByAssessments() {
        // IDs
        final UUID assessmentRequirementID1 = UUID.randomUUID();
        final String assessmentCode1 = "Test1";
        final String assessmentName1 = "Test1 Name description";
        final String ruleCode1 = "ruleCode1";
        final UUID assessmentRequirementID2 = UUID.randomUUID();
        final String assessmentCode2 = "Test2";
        final String assessmentName2 = "Test2 Name description";
        final String ruleCode2 = "ruleCode2";

        // Input
        final AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessmentCodes(Arrays.asList(assessmentCode1, assessmentCode2));

        // Output
        final List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        final AssessmentRequirement assmtReq1 = new AssessmentRequirement();
        assmtReq1.setAssessmentRequirementId(assessmentRequirementID1);
        assmtReq1.setAssessmentCode(assessmentCode1);
        assmtReq1.setAssessmentName(assessmentName1);
        assmtReq1.setRuleCode(ruleCode1);
        assessmentReqList.add(assmtReq1);

        final AssessmentRequirement assmtReq2 = new AssessmentRequirement();
        assmtReq2.setAssessmentRequirementId(assessmentRequirementID2);
        assmtReq2.setAssessmentCode(assessmentCode2);
        assmtReq2.setAssessmentName(assessmentName2);
        assmtReq2.setRuleCode(ruleCode2);
        assessmentReqList.add(assmtReq2);

        when(assessmentRequirementTransformer.transformToDTO(assessmentRequirementRepository.findByAssessmentCodeIn(assessmentList.getAssessmentCodes()))).thenReturn(assessmentReqList);

        var result = assessmentRequirementService.getAssessmentRequirementListByAssessments(assessmentList);

        assertThat(result).isNotNull();
        assertThat(result.getAssessmentRequirementList().isEmpty()).isFalse();
        assertThat(result.getAssessmentRequirementList().size()).isEqualTo(2);
    }

}
