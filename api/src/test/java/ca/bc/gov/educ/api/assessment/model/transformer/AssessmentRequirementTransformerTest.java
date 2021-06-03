package ca.bc.gov.educ.api.assessment.model.transformer;

import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
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
        asmtrEntity.setRuleCode("RuleCd");

        AssessmentRequirement asmtr = new AssessmentRequirement();
        asmtr.setAssessmentRequirementId(asmtrEntity.getAssessmentRequirementId());
        asmtr.setAssessmentCode(asmtrEntity.getAssessmentCode());
        asmtr.setRuleCode(asmtrEntity.getRuleCode());

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
        asmtrEntity.setRuleCode("RuleCd");

        AssessmentRequirement asmtr = new AssessmentRequirement();
        asmtr.setAssessmentRequirementId(asmtrEntity.getAssessmentRequirementId());
        asmtr.setAssessmentCode(asmtrEntity.getAssessmentCode());
        asmtr.setRuleCode(asmtrEntity.getRuleCode());

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
        asmtr.setRuleCode("RuleCd");

        AssessmentRequirementEntity asmtrEntity = new AssessmentRequirementEntity();
        asmtrEntity.setAssessmentRequirementId(asmtr.getAssessmentRequirementId());
        asmtrEntity.setAssessmentCode(asmtr.getAssessmentCode());
        asmtrEntity.setRuleCode(asmtr.getRuleCode());

        Mockito.when(modelMapper.map(asmtr, AssessmentRequirementEntity.class)).thenReturn(asmtrEntity);
        var result = assessmentRequirementTransformer.transformToEntity(asmtr);
        assertThat(result).isNotNull();
        assertThat(result.getAssessmentRequirementId()).isEqualTo(asmtrEntity.getAssessmentRequirementId());


    }

}
