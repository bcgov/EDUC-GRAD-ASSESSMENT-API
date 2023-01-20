package ca.bc.gov.educ.api.assessment.config;

import ca.bc.gov.educ.api.assessment.util.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Component
@Slf4j
public class RequestResponseInterceptor implements AsyncHandlerInterceptor {

    EducAssessmentApiConstants constants;
    GradValidation validation;

    @Autowired
    public RequestResponseInterceptor(EducAssessmentApiConstants constants, GradValidation validation) {
        this.constants = constants;
        this.validation = validation;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // for async this is called twice so need a check to avoid setting twice.
        if (request.getAttribute("startTime") == null) {
            final long startTime = Instant.now().toEpochMilli();
            request.setAttribute("startTime", startTime);
        }

        validation.clear();

        // username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) auth;
            Jwt jwt = (Jwt) authenticationToken.getCredentials();
            String username = JwtUtil.getName(jwt);
            if (username != null) {
                ThreadLocalStateUtil.setCurrentUser(username);
            }
        }
        return true;
    }

    /**
     * After completion.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @param ex       the ex
     */
    @Override
    public void afterCompletion(@NonNull final HttpServletRequest request, final HttpServletResponse response, @NonNull final Object handler, final Exception ex) {
        LogHelper.logServerHttpReqResponseDetails(request, response, constants.isSplunkLogHelperEnabled());
        val correlationID = request.getHeader(constants.CORRELATION_ID);
        if (correlationID != null) {
            response.setHeader(constants.CORRELATION_ID, request.getHeader(constants.CORRELATION_ID));
        }
    }

}
