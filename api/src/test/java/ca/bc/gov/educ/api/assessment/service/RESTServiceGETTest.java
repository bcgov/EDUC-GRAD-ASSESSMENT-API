package ca.bc.gov.educ.api.assessment.service;

import ca.bc.gov.educ.api.assessment.exception.ServiceException;
import ca.bc.gov.educ.api.assessment.model.dto.GradRuleDetails;
import io.netty.channel.ConnectTimeoutException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class RESTServiceGETTest {

    @Autowired
    private RESTService restService;

    @MockBean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    @MockBean
    public OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @MockBean
    public ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    @Qualifier("assessmentApiClient")
    public WebClient assessmentApiWebClient;

    @MockBean
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @MockBean
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @MockBean
    private WebClient.RequestBodySpec requestBodyMock;
    @MockBean
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @MockBean
    private WebClient.ResponseSpec responseMock;

    private static final String TEST_URL_200 = "https://httpstat.us/200";
    private static final String TEST_URL_403 = "https://httpstat.us/403";
    private static final String TEST_URL_503 = "https://httpstat.us/503";

    private static final String ACCESS_TOKEN = "123";
    private static final String OK_RESPONSE = "200 OK";
    private static final ParameterizedTypeReference<List<GradRuleDetails>> refType = new ParameterizedTypeReference<List<GradRuleDetails>>() {
    };

    @Before
    public void setUp(){
        when(this.assessmentApiWebClient.get()).thenReturn(this.requestHeadersUriMock);
        when(this.requestHeadersUriMock.uri(any(String.class))).thenReturn(this.requestHeadersMock);
        when(this.requestHeadersMock.headers(any(Consumer.class))).thenReturn(this.requestHeadersMock);
        when(this.requestHeadersMock.retrieve()).thenReturn(this.responseMock);
        when(this.responseMock.onStatus(any(), any())).thenReturn(this.responseMock);
    }

    @Test
    public void testGet_GivenProperData_Expect200Response(){
        when(this.responseMock.bodyToMono(String.class)).thenReturn(Mono.just(OK_RESPONSE));
        String response = this.restService.get(TEST_URL_200, String.class, assessmentApiWebClient);
        Assert.assertEquals(OK_RESPONSE, response);
    }

    @Test
    public void testGet_GivenNullWebClient_Expect200Response(){
        when(this.responseMock.bodyToMono(String.class)).thenReturn(Mono.just(OK_RESPONSE));
        String response = this.restService.get(TEST_URL_200, String.class, null);
        Assert.assertEquals(OK_RESPONSE, response);
    }

    @Test
    public void testGetTypeRef_GivenProperData_Expect200Response(){
        when(this.responseMock.bodyToMono(refType)).thenReturn(Mono.just(new ArrayList<GradRuleDetails>()));
        List<GradRuleDetails> response = this.restService.get(TEST_URL_200, refType, assessmentApiWebClient);
        Assert.assertEquals(new ArrayList<String>(), response);
    }

    @Test
    public void testGetTypeRef_GivenNullWebClient_Expect200Response(){
        when(this.responseMock.bodyToMono(refType)).thenReturn(Mono.just(new ArrayList<GradRuleDetails>()));
        List<GradRuleDetails> response = this.restService.get(TEST_URL_200, refType, null);
        Assert.assertEquals(new ArrayList<String>(), response);
    }

    @Test
    public void testGetOverride_GivenProperData_Expect200Response(){
        when(this.responseMock.bodyToMono(String.class)).thenReturn(Mono.just(OK_RESPONSE));
        String response = this.restService.get(TEST_URL_200, String.class, assessmentApiWebClient);
        Assert.assertEquals(OK_RESPONSE, response);
    }

    @Test(expected = ServiceException.class)
    public void testGet_Given5xxErrorFromService_ExpectServiceError(){
        when(this.responseMock.bodyToMono(ServiceException.class)).thenReturn(Mono.just(new ServiceException()));
        this.restService.get(TEST_URL_503, String.class, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGetTypeRef_Given5xxErrorFromService_ExpectServiceError(){
        when(this.responseMock.bodyToMono(refType)).thenThrow(new ServiceException());
        this.restService.get(TEST_URL_503, refType, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGetOverride_Given5xxErrorFromService_ExpectServiceError(){
        when(this.responseMock.bodyToMono(ServiceException.class)).thenReturn(Mono.just(new ServiceException()));
        this.restService.get(TEST_URL_503, String.class, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGet_Given4xxErrorFromService_ExpectServiceError(){
        when(this.responseMock.bodyToMono(ServiceException.class)).thenReturn(Mono.just(new ServiceException()));
        this.restService.get(TEST_URL_403, String.class, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGetTypeRef_Given4xxErrorFromService_ExpectServiceError(){
        when(this.responseMock.bodyToMono(ServiceException.class)).thenReturn(Mono.just(new ServiceException()));
        this.restService.get(TEST_URL_403, refType, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGetOverride_Given4xxErrorFromService_ExpectServiceError(){
        when(this.responseMock.bodyToMono(ServiceException.class)).thenReturn(Mono.just(new ServiceException()));
        this.restService.get(TEST_URL_403, String.class, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGet_Given5xxErrorFromService_ExpectConnectionError(){
        when(requestBodyUriMock.uri(TEST_URL_503)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);

        Throwable cause = new RuntimeException("Simulated cause");
        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.error(new ConnectTimeoutException("Connection closed")));
        restService.get(TEST_URL_503, String.class, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGet_Given5xxErrorFromService_ExpectWebClientRequestError(){
        when(requestBodyUriMock.uri(TEST_URL_503)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);

        Throwable cause = new RuntimeException("Simulated cause");
        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.error(new WebClientRequestException(cause, HttpMethod.GET, null, new HttpHeaders())));
        restService.get(TEST_URL_503, String.class, assessmentApiWebClient);
    }

    @Test(expected = ServiceException.class)
    public void testGetTypeRef_Given5xxErrorFromService_ExpectWebClientRequestError(){
        when(requestBodyUriMock.uri(TEST_URL_503)).thenReturn(requestBodyMock);
        when(requestBodyMock.retrieve()).thenReturn(responseMock);

        Throwable cause = new RuntimeException("Simulated cause");
        when(responseMock.bodyToMono(String.class)).thenReturn(Mono.error(new WebClientRequestException(cause, HttpMethod.GET, null, new HttpHeaders())));
        restService.get(TEST_URL_503, refType, assessmentApiWebClient);
    }

}
