package ca.bc.gov.educ.api.assessment;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;

@SpringBootApplication
public class EducAssessmentApiApplication {

	private static Logger logger = LoggerFactory.getLogger(EducAssessmentApiApplication.class);

	public static void main(String[] args) {
		logger.debug("########Starting API");
		SpringApplication.run(EducAssessmentApiApplication.class, args);
		logger.debug("########Started API");
	}

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.typeMap(AssessmentEntity.class, Assessment.class);
		modelMapper.typeMap(Assessment.class, AssessmentEntity.class);
		return modelMapper;
	}
}