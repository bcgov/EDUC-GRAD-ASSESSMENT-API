package ca.bc.gov.educ.api.assessment.model.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;

@Component
public class AssessmentTransformer {

    @Autowired
    ModelMapper modelMapper;

    public Assessment transformToDTO (AssessmentEntity assmtEntity) {
        return modelMapper.map(assmtEntity, Assessment.class);
    }

    public Assessment transformToDTO ( Optional<AssessmentEntity> assessmentEntity ) {
        AssessmentEntity cae = new AssessmentEntity();

        if (assessmentEntity.isPresent())
            cae = assessmentEntity.get();
        return modelMapper.map(cae, Assessment.class);
    }

	public List<Assessment> transformToDTO (Iterable<AssessmentEntity> assmtEntities ) {

        List<Assessment> assmtList = new ArrayList<>();

        for (AssessmentEntity assmtEntity : assmtEntities) {
            Assessment assessment = modelMapper.map(assmtEntity, Assessment.class);
            assmtList.add(assessment);
        }

        return assmtList;
    }

    public AssessmentEntity transformToEntity(Assessment assessment) {
        return modelMapper.map(assessment, AssessmentEntity.class);
    }
}
