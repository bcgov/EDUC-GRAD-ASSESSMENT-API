package ca.bc.gov.educ.api.assessment.model.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.educ.api.assessment.model.dto.AssessmentRequirement;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentRequirementEntity;

@Component
public class AssessmentRequirementTransformer {

    @Autowired
    ModelMapper modelMapper;

    public AssessmentRequirement transformToDTO (AssessmentRequirementEntity assessmentRequirementEntity) {
        return modelMapper.map(assessmentRequirementEntity, AssessmentRequirement.class);
    }

    public AssessmentRequirement transformToDTO ( Optional<AssessmentRequirementEntity> assessmentRequirementEntity ) {
        AssessmentRequirementEntity cae = new AssessmentRequirementEntity();
        if (assessmentRequirementEntity.isPresent())
            cae = assessmentRequirementEntity.get();
        return modelMapper.map(cae, AssessmentRequirement.class);
    }

	public List<AssessmentRequirement> transformToDTO (Iterable<AssessmentRequirementEntity> assessmentReqEntities ) {

        List<AssessmentRequirement> courseReqList = new ArrayList<>();

        for (AssessmentRequirementEntity assessmentReqEntity : assessmentReqEntities) {
            AssessmentRequirement courseRequirement = modelMapper.map(assessmentReqEntity, AssessmentRequirement.class);
            courseReqList.add(courseRequirement);
        }

        return courseReqList;
    }

    public AssessmentRequirementEntity transformToEntity(AssessmentRequirement assessmentRequirement) {
        return modelMapper.map(assessmentRequirement, AssessmentRequirementEntity.class);
    }
}
