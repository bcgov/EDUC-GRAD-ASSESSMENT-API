package ca.bc.gov.educ.api.assessment.controller;

import ca.bc.gov.educ.api.assessment.model.dto.*;
import ca.bc.gov.educ.api.assessment.service.AssessmentRequirementService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class AssessmentControllerTest {
    @Mock
    private AssessmentService assessmentService;

    @Mock
    private AssessmentRequirementService assessmentRequirementService;

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

    @Test
    public void testGetAllAssessmentRequirement() {
        // ID
        final UUID assessmentRequirementID = UUID.randomUUID();
        final String assessmentCode = "TEST";
        final String assessmentName = "TEST Name description";
        final String ruleCode = "RULE";
        final String requirementName = "ReqName";
        final String requirementProgram = "ReqProg";

        final List<AllAssessmentRequirements> allAssessmentRequirementsList = new ArrayList<>();
        AllAssessmentRequirements aar = new AllAssessmentRequirements();
        aar.setAssessmentRequirementId(assessmentRequirementID);
        aar.setAssessmentCode(assessmentCode);
        aar.setAssessmentName(assessmentName);
        aar.setRequirementName(requirementName);
        aar.setRequirementProgram(requirementProgram);
        aar.setRuleCode(ruleCode);
        allAssessmentRequirementsList.add(aar);

        Authentication authentication = Mockito.mock(Authentication.class);
        OAuth2AuthenticationDetails details = Mockito.mock(OAuth2AuthenticationDetails.class);
        // Mockito.whens() for your authorization object
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getDetails()).thenReturn(details);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(assessmentRequirementService.getAllAssessmentRequirementList(1,5, null)).thenReturn(allAssessmentRequirementsList);
        assessmentController.getAllAssessmentRequirement(1,5);
        Mockito.verify(assessmentRequirementService).getAllAssessmentRequirementList(1,5, null);
    }

    @Test
    public void testGetAllAssessmentRequirementByRule() {
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
        assmtReq.setAssessmentName(assessmentName);
        assmtReq.setRuleCode(ruleCode);
        assessmentReqList.add(assmtReq);

        Mockito.when(assessmentRequirementService.getAllAssessmentRequirementListByRule(ruleCode, 1,5)).thenReturn(assessmentReqList);
        assessmentController.getAllAssessmentRequirementByRule(ruleCode, 1,5);
        Mockito.verify(assessmentRequirementService).getAllAssessmentRequirementListByRule(ruleCode, 1, 5);
    }

    @Test
    public void testGetAssessmentRequirementByAssessments() {
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
        final AssessmentRequirements assessmentRequirements = new AssessmentRequirements();
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

        assessmentRequirements.setAssessmentRequirementList(assessmentReqList);

        Mockito.when(assessmentRequirementService.getAssessmentRequirementListByAssessments(assessmentList)).thenReturn(assessmentRequirements);
        assessmentController.getAssessmentRequirementByAssessments(assessmentList);
        Mockito.verify(assessmentRequirementService).getAssessmentRequirementListByAssessments(assessmentList);
    }


}
