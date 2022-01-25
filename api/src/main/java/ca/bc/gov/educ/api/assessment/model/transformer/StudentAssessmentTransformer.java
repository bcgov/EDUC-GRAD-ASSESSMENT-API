package ca.bc.gov.educ.api.assessment.model.transformer;

import ca.bc.gov.educ.api.assessment.model.dto.StudentAssessment;
import ca.bc.gov.educ.api.assessment.model.entity.StudentAssessmentEntity;
import ca.bc.gov.educ.api.assessment.util.GradAssessmentApiUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StudentAssessmentTransformer {

    @Autowired
    ModelMapper modelMapper;

    public StudentAssessment transformToDTO (StudentAssessmentEntity studentAssessmentEntity) {
        return modelMapper.map(studentAssessmentEntity, StudentAssessment.class);
    }

    public StudentAssessment transformToDTO ( Optional<StudentAssessmentEntity> studentAssessmentEntity ) {
        StudentAssessmentEntity cae = new StudentAssessmentEntity();

        if (studentAssessmentEntity.isPresent())
            cae = studentAssessmentEntity.get();
        return modelMapper.map(cae, StudentAssessment.class);
    }

    public List<StudentAssessment> transformToDTO (Iterable<StudentAssessmentEntity> studentAssessmentEntities ) {

        List<StudentAssessment> studentAssessmentList = new ArrayList<>();

        for (StudentAssessmentEntity studentAssessmentEntity : studentAssessmentEntities) {
            StudentAssessment studentAssessment = modelMapper.map(studentAssessmentEntity, StudentAssessment.class);
            studentAssessment.setPen(studentAssessmentEntity.getAssessmentKey().getPen());
            studentAssessment.setAssessmentCode(studentAssessmentEntity.getAssessmentKey().getAssessmentCode());
            studentAssessment.setSessionDate(GradAssessmentApiUtils.parseTraxDate(studentAssessmentEntity.getAssessmentKey().getSessionDate()));
            studentAssessmentList.add(studentAssessment);
        }

        return studentAssessmentList;
    }

    public StudentAssessmentEntity transformToEntity(StudentAssessment studentAssessment) {
        return modelMapper.map(studentAssessment, StudentAssessmentEntity.class);
    }
}
