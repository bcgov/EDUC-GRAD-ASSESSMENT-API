package ca.bc.gov.educ.api.assessment.config;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EducGradAssessmentApiConfig {

    EducAssessmentApiConstants constants;

    @Autowired
    public EducGradAssessmentApiConfig(EducAssessmentApiConstants constants) {
        this.constants = constants;
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(AssessmentEntity.class, Assessment.class);
        modelMapper.typeMap(Assessment.class, AssessmentEntity.class);
        return modelMapper;
    }
}
