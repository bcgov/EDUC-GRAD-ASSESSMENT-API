package ca.bc.gov.educ.api.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.*;
import java.util.function.Consumer;

import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRepository;
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

import ca.bc.gov.educ.api.assessment.model.dto.AllAssessmentRequirements;
import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentList;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.dto.GradRuleDetails;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementCodeEntity;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementCodeRepository;
import ca.bc.gov.educ.api.assessment.repository.AssessmentRequirementRepository;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AssessmentRequirementServiceTest {

    @Autowired
    EducAssessmentApiConstants constants;

    @Autowired
    private AssessmentRequirementService assessmentRequirementService;

    @Autowired
    private AssessmentService assessmentService;

    @MockBean
    private AssessmentRequirementRepository assessmentRequirementRepository;

    @MockBean
    private AssessmentRepository assessmentRepository;

    @MockBean
    private AssessmentRequirementCodeRepository assessmentRequirementCodeRepository;

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
        assessmentRequirementCodeRepository.save(createAssessmentRuleCode());
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
        final String ruleCode = "116";
        final String requirementName = "ReqName";
        final String requirementProgram = "ReqProg";

        // Assessment Requirements Entity
        final List<AssessmentRequirementEntity> assessmentReqEntityList = new ArrayList<>();
        final AssessmentRequirementEntity assmtReqEntity = new AssessmentRequirementEntity();
        assmtReqEntity.setAssessmentRequirementId(assessmentRequirementID);
        assmtReqEntity.setAssessmentCode(assessmentCode);
        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode("116");
        assmtReqEntity.setRuleCode(code);
        assessmentReqEntityList.add(assmtReqEntity);

        Pageable paging = PageRequest.of(1, 5);
        Page<AssessmentRequirementEntity> pageResult = new PageImpl<AssessmentRequirementEntity>(assessmentReqEntityList);

        // Assessment
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode(assessmentCode);
        assmt.setAssessmentName(assessmentName);

        final AssessmentEntity assmtent = new AssessmentEntity();
        assmtent.setAssessmentCode(assessmentCode);
        assmtent.setAssessmentName(assessmentName);

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
        when(assessmentRepository.findByAssessmentCode(assmt.getAssessmentCode())).thenReturn(Optional.of(assmtent));

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

        // Assessment Requirements Entity
        final List<AssessmentRequirementEntity> assessmentReqEntityList = new ArrayList<>();
        final AssessmentRequirementEntity assmtReqEntity = new AssessmentRequirementEntity();
        assmtReqEntity.setAssessmentRequirementId(assessmentRequirementID);
        assmtReqEntity.setAssessmentCode(assessmentCode);
        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode("116");
        assmtReqEntity.setRuleCode(code);
        assessmentReqEntityList.add(assmtReqEntity);

        Pageable paging = PageRequest.of(1, 5);
        Page<AssessmentRequirementEntity> pageResult = new PageImpl<AssessmentRequirementEntity>(assessmentReqEntityList);

        // Assessment
        final Assessment assmt = new Assessment();
        assmt.setAssessmentCode(assessmentCode);
        assmt.setAssessmentName(assessmentName);

        when(assessmentRequirementRepository.findByRuleCode(assessmentRequirementCodeRepository.getOne(ruleCode), paging)).thenReturn(pageResult);

        var result = assessmentRequirementService.getAllAssessmentRequirementListByRule(ruleCode, 1, 5);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        AssessmentRequirement ar = result.get(0);
        assertThat(ar.getAssessmentCode()).isEqualTo(assessmentCode);
    }

    @Test
    public void testGetAssessmentRequirementListByAssessments() {
        // IDs
        final UUID assessmentRequirementID1 = UUID.randomUUID();
        final String assessmentCode1 = "Test1";
        final String ruleCode1 = "ruleCode1";
        final UUID assessmentRequirementID2 = UUID.randomUUID();
        final String assessmentCode2 = "Test2";
        final String ruleCode2 = "ruleCode2";

        // Input
        final AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessmentCodes(Arrays.asList(assessmentCode1, assessmentCode2));

        // Output
        final List<AssessmentRequirementEntity> assessmentReqList = new ArrayList<>();
        final AssessmentRequirementEntity assmtReq1 = new AssessmentRequirementEntity();
        assmtReq1.setAssessmentRequirementId(assessmentRequirementID1);
        assmtReq1.setAssessmentCode(assessmentCode1);
        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode("116");
        assmtReq1.setRuleCode(code);
        assessmentReqList.add(assmtReq1);

        final AssessmentRequirementEntity assmtReq2 = new AssessmentRequirementEntity();
        assmtReq2.setAssessmentRequirementId(assessmentRequirementID2);
        assmtReq2.setAssessmentCode(assessmentCode2);
        AssessmentRequirementCodeEntity code2 = new AssessmentRequirementCodeEntity();
        code2.setAssmtRequirementCode("116");
        assmtReq2.setRuleCode(code2);
        assessmentReqList.add(assmtReq2);

        when(assessmentRequirementRepository.findByAssessmentCodeIn(assessmentList.getAssessmentCodes())).thenReturn(assessmentReqList);

        var result = assessmentService.getAssessmentRequirementListByAssessments(assessmentList);

        assertThat(result).isNotNull();
        assertThat(result.getAssessmentRequirementList().isEmpty()).isFalse();
        assertThat(result.getAssessmentRequirementList().size()).isEqualTo(2);
    }

    @Test
    public void testCreateAssessmentRequirement() {
        final UUID assessmentRequirementID = UUID.randomUUID();
        final String assessmentCode = "TestCode";
        final String ruleCode = "ruleCode";

        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode(ruleCode);
        code.setLabel("Rule Test Label");
        code.setDescription("Rule Test Description");
        code.setEffectiveDate(new java.sql.Date(System.currentTimeMillis() - 10000L));
        code.setExpiryDate(new java.sql.Date(System.currentTimeMillis() + 100000L));
        code.setCreateUser("ASSESSMENT");
        code.setUpdateUser("ASSESSMENT");
        code.setCreateDate(new Date());
        code.setUpdateDate(new Date());

        AssessmentRequirementEntity assmtReq = new AssessmentRequirementEntity();
        assmtReq.setAssessmentRequirementId(assessmentRequirementID);
        assmtReq.setAssessmentCode(assessmentCode);
        assmtReq.setRuleCode(code);

        AssessmentRequirementEntity saved = new AssessmentRequirementEntity();
        saved.setAssessmentRequirementId(assessmentRequirementID);
        saved.setAssessmentCode(assessmentCode);
        saved.setRuleCode(code);
        assmtReq.setCreateUser("ASSESSMENT");
        assmtReq.setUpdateUser("ASSESSMENT");
        assmtReq.setCreateDate(new Date());
        assmtReq.setUpdateDate(new Date());

        when(assessmentRequirementRepository.findByAssessmentCodeAndRuleCode(assessmentCode, code)).thenReturn(assmtReq);
        when(assessmentRequirementCodeRepository.findById(ruleCode)).thenReturn(Optional.of(code));
        when(assessmentRequirementRepository.save(assmtReq)).thenReturn(saved);

        var result = assessmentRequirementService.createAssessmentRequirement(assessmentCode, ruleCode);
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentCode()).isEqualTo(assessmentCode);
        assertThat(result.getRuleCode().getAssmtRequirementCode()).isEqualTo(ruleCode);

    }

    private AssessmentRequirementCodeEntity createAssessmentRuleCode() {
		AssessmentRequirementCodeEntity data = new AssessmentRequirementCodeEntity();
		data.setAssmtRequirementCode("116");
		data.setDescription("sadad");
		return data;
	}
}
