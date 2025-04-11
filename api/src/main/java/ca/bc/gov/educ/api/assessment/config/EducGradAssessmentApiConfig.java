package ca.bc.gov.educ.api.assessment.config;

import ca.bc.gov.educ.api.assessment.model.dto.Assessment;
import ca.bc.gov.educ.api.assessment.model.entity.AssessmentEntity;
import ca.bc.gov.educ.api.assessment.util.EducAssessmentApiConstants;
import ca.bc.gov.educ.api.assessment.util.LogHelper;
import ca.bc.gov.educ.api.assessment.util.ThreadLocalStateUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

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

    @Bean
    public WebClient webClient() {
        HttpClient client = HttpClient.create();
        client.warmup().block();
        return WebClient.builder()
                .filter(setRequestHeaders())
                .filter(this.log())
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

    private ExchangeFilterFunction log() {
        return (clientRequest, next) -> next
                .exchange(clientRequest)
                .doOnNext((clientResponse -> LogHelper.logClientHttpReqResponseDetails(
                        clientRequest.method(),
                        clientRequest.url().toString(),
                        clientResponse.statusCode().value(),
                        clientRequest.headers().get(EducAssessmentApiConstants.CORRELATION_ID),
                        clientRequest.headers().get(EducAssessmentApiConstants.REQUEST_SOURCE),
                        constants.isSplunkLogHelperEnabled())
                ));
    }
}
