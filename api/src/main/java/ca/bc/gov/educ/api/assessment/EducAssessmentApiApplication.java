package ca.bc.gov.educ.api.assessment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EducAssessmentApiApplication {

    private static Logger logger = LoggerFactory.getLogger(EducAssessmentApiApplication.class);

    public static void main(String[] args) {
        logger.debug("########Starting API");
        SpringApplication.run(EducAssessmentApiApplication.class, args);
        logger.debug("########Started API");
    }
}