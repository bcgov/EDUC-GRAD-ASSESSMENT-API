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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class AssessmentRequirementControllerTest {

    @Mock
    private AssessmentRequirementService assessmentRequirementService;

    @Mock
    private AssessmentService assessmentService;

    @Mock
    ResponseHelper responseHelper;

    @Mock
    GradValidation validation;

    @InjectMocks
    private AssessmentRequirementController assessmentRequirementController;

    @Test
    public void testGetAllAssessmentRequirementByRule() {
        // ID
        final UUID assessmentRequirementID = UUID.randomUUID();
        final String assessmentCode = "TEST";
        final String ruleCode = "RULE";

        // Assessment Requirements
        final List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        final AssessmentRequirement assmtReq = new AssessmentRequirement();
        assmtReq.setAssessmentRequirementId(assessmentRequirementID);
        assmtReq.setAssessmentCode(assessmentCode);
        AssessmentRequirementCode code = new AssessmentRequirementCode();
        code.setAssmtRequirementCode("116");
        assmtReq.setRuleCode(code);
        assessmentReqList.add(assmtReq);

        Mockito.when(assessmentRequirementService.getAllAssessmentRequirementListByRule(ruleCode, 1,5)).thenReturn(assessmentReqList);
        assessmentRequirementController.getAllAssessmentRequirementByRule(ruleCode, 1,5);
        Mockito.verify(assessmentRequirementService).getAllAssessmentRequirementListByRule(ruleCode, 1, 5);
    }

    @Test
    public void testGetAssessmentRequirementByAssessments() {
        // IDs
        final UUID assessmentRequirementID1 = UUID.randomUUID();
        final String assessmentCode1 = "Test1";
        final UUID assessmentRequirementID2 = UUID.randomUUID();
        final String assessmentCode2 = "Test2";

        // Input
        final AssessmentList assessmentList = new AssessmentList();
        assessmentList.setAssessmentCodes(Arrays.asList(assessmentCode1, assessmentCode2));

        // Output
        final AssessmentRequirements assessmentRequirements = new AssessmentRequirements();
        final List<AssessmentRequirement> assessmentReqList = new ArrayList<>();
        final AssessmentRequirement assmtReq1 = new AssessmentRequirement();
        assmtReq1.setAssessmentRequirementId(assessmentRequirementID1);
        assmtReq1.setAssessmentCode(assessmentCode1);
        AssessmentRequirementCode code = new AssessmentRequirementCode();
        code.setAssmtRequirementCode("116");
        assmtReq1.setRuleCode(code);
        assessmentReqList.add(assmtReq1);

        final AssessmentRequirement assmtReq2 = new AssessmentRequirement();
        assmtReq2.setAssessmentRequirementId(assessmentRequirementID2);
        assmtReq2.setAssessmentCode(assessmentCode2);
        AssessmentRequirementCode code2 = new AssessmentRequirementCode();
        code2.setAssmtRequirementCode("116");
        assmtReq2.setRuleCode(code2);
        assessmentReqList.add(assmtReq2);

        assessmentRequirements.setAssessmentRequirementList(assessmentReqList);

        Mockito.when(assessmentService.getAssessmentRequirementListByAssessments(assessmentList)).thenReturn(assessmentRequirements);
        assessmentRequirementController.getAssessmentRequirementByAssessments(assessmentList);
        Mockito.verify(assessmentService).getAssessmentRequirementListByAssessments(assessmentList);
    }


    @Test
    public void testCreateAssessmentRequirement() {
        // IDs
        final UUID assessmentRequirementID1 = UUID.randomUUID();
        final String assessmentCode = "Test1";
        final String ruleCode = "ruleCode1";

        // Input
        final AssessmentRequirement assmtReq = new AssessmentRequirement();
        assmtReq.setAssessmentRequirementId(assessmentRequirementID1);
        assmtReq.setAssessmentCode(assessmentCode);
        AssessmentRequirementCode code = new AssessmentRequirementCode();
        code.setAssmtRequirementCode(ruleCode);
        assmtReq.setRuleCode(code);

        Mockito.when(assessmentRequirementService.createAssessmentRequirement(assessmentCode, ruleCode)).thenReturn(assmtReq);
        assessmentRequirementController.createAssessmentRequirement(assmtReq);
        Mockito.verify(assessmentRequirementService).createAssessmentRequirement(assessmentCode, ruleCode);
    }
}
