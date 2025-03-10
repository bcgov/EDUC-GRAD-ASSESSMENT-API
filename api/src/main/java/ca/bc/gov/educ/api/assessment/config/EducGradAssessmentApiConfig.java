package ca.bc.gov.educ.api.assessment.config;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import ca.bc.gov.educ.api.assessment.util.ThreadLocalStateUtil;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.netty.http.client.HttpClient;

@Configuration
public class EducGradAssessmentApiConfig {

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(AssessmentEntity.class, Assessment.class);
        modelMapper.typeMap(Assessment.class, AssessmentEntity.class);
        return modelMapper;
    }

    @Bean
    public WebClient webClient() {
        HttpClient client = HttpClient.create();
        client.warmup().block();
        return WebClient.builder()
                .filter(setRequestHeaders())
                .build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    private ExchangeFilterFunction setRequestHeaders() {
        return (clientRequest, next) -> {
            ClientRequest modifiedRequest = ClientRequest.from(clientRequest)
                    .header(EducAssessmentApiConstants.CORRELATION_ID, ThreadLocalStateUtil.getCorrelationID())
                    .header(EducAssessmentApiConstants.USER_NAME, ThreadLocalStateUtil.getCurrentUser())
                    .header(EducAssessmentApiConstants.REQUEST_SOURCE, EducAssessmentApiConstants.API_NAME)
                    .build();
            return next.exchange(modifiedRequest);
        };
    }

}
