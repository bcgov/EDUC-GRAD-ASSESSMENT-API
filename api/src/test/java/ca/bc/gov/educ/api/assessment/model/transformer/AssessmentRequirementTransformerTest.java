package ca.bc.gov.educ.api.assessment.model.transformer;

import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirementCode;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementCodeEntity;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class AssessmentRequirementTransformerTest {
    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    AssessmentRequirementTransformer assessmentRequirementTransformer;

    @Test
    public void testTransformToDTO() {
        AssessmentRequirementEntity asmtrEntity = new AssessmentRequirementEntity();
        asmtrEntity.setAssessmentRequirementId(UUID.randomUUID());
        asmtrEntity.setAssessmentCode("TEST");
        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode("116");
        asmtrEntity.setRuleCode(code);

        AssessmentRequirement asmtr = new AssessmentRequirement();
        asmtr.setAssessmentRequirementId(asmtrEntity.getAssessmentRequirementId());
        asmtr.setAssessmentCode(asmtrEntity.getAssessmentCode());
        AssessmentRequirementCode code2 = new AssessmentRequirementCode();
        code2.setAssmtRequirementCode("116");
        asmtr.setRuleCode(code2);

        Mockito.when(modelMapper.map(asmtrEntity, AssessmentRequirement.class)).thenReturn(asmtr);
        var result = assessmentRequirementTransformer.transformToDTO(asmtrEntity);
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentRequirementId()).isEqualTo(asmtr.getAssessmentRequirementId());
    }

    @Test
    public void testTransformToDTOWithOptional() {
        AssessmentRequirementEntity asmtrEntity = new AssessmentRequirementEntity();
        asmtrEntity.setAssessmentRequirementId(UUID.randomUUID());
        asmtrEntity.setAssessmentCode("TEST");
        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode("116");
        asmtrEntity.setRuleCode(code);

        AssessmentRequirement asmtr = new AssessmentRequirement();
        asmtr.setAssessmentRequirementId(asmtrEntity.getAssessmentRequirementId());
        asmtr.setAssessmentCode(asmtrEntity.getAssessmentCode());
        AssessmentRequirementCode code2 = new AssessmentRequirementCode();
        code2.setAssmtRequirementCode("116");
        asmtr.setRuleCode(code2);

        Mockito.when(modelMapper.map(asmtrEntity, AssessmentRequirement.class)).thenReturn(asmtr);
        var result = assessmentRequirementTransformer.transformToDTO(Optional.of(asmtrEntity));
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentRequirementId()).isEqualTo(asmtr.getAssessmentRequirementId());
    }

    @Test
    public void testTransformToEntity() {
        AssessmentRequirement asmtr = new AssessmentRequirement();
        asmtr.setAssessmentRequirementId(UUID.randomUUID());
        asmtr.setAssessmentCode("TEST");
        AssessmentRequirementCode code2 = new AssessmentRequirementCode();
        code2.setAssmtRequirementCode("116");
        asmtr.setRuleCode(code2);

        AssessmentRequirementEntity asmtrEntity = new AssessmentRequirementEntity();
        asmtrEntity.setAssessmentRequirementId(asmtr.getAssessmentRequirementId());
        asmtrEntity.setAssessmentCode(asmtr.getAssessmentCode());
        AssessmentRequirementCodeEntity code = new AssessmentRequirementCodeEntity();
        code.setAssmtRequirementCode("116");
        asmtrEntity.setRuleCode(code);

        Mockito.when(modelMapper.map(asmtr, AssessmentRequirementEntity.class)).thenReturn(asmtrEntity);
        var result = assessmentRequirementTransformer.transformToEntity(asmtr);
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentRequirementId()).isEqualTo(asmtrEntity.getAssessmentRequirementId());


    }

}
