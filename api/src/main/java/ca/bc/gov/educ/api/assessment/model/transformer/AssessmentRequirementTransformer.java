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

    public AssessmentRequirement transformToDTO (AssessmentRequirementEntity courseRequirementEntity) {
        AssessmentRequirement courseRequirement = modelMapper.map(courseRequirementEntity, AssessmentRequirement.class);
        return courseRequirement;
    }

    public AssessmentRequirement transformToDTO ( Optional<AssessmentRequirementEntity> courseRequirementEntity ) {
        AssessmentRequirementEntity cae = new AssessmentRequirementEntity();

        if (courseRequirementEntity.isPresent())
            cae = courseRequirementEntity.get();

        AssessmentRequirement courseRequirement = modelMapper.map(cae, AssessmentRequirement.class);
        return courseRequirement;
    }

	public List<AssessmentRequirement> transformToDTO (Iterable<AssessmentRequirementEntity> courseReqEntities ) {

        List<AssessmentRequirement> courseReqList = new ArrayList<AssessmentRequirement>();

        for (AssessmentRequirementEntity courseReqEntity : courseReqEntities) {
            AssessmentRequirement courseRequirement = new AssessmentRequirement();
            courseRequirement = modelMapper.map(courseReqEntity, AssessmentRequirement.class); 
            courseReqList.add(courseRequirement);
        }

        return courseReqList;
    }

    public AssessmentRequirementEntity transformToEntity(AssessmentRequirement courseRequirement) {
        AssessmentRequirementEntity courseRequirementEntity = modelMapper.map(courseRequirement, AssessmentRequirementEntity.class);
        return courseRequirementEntity;
    }
}
