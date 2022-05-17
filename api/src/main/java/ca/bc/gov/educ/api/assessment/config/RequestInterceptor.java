package ca.bc.gov.educ.api.assessment.config;

import ca.bc.gov.educ.api.assessment.util.GradValidation;
import ca.bc.gov.educ.api.assessment.util.ThreadLocalStateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	GradValidation validation;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// for async this is called twice so need a check to avoid setting twice.
		if (request.getAttribute("startTime") == null) {
			final long startTime = Instant.now().toEpochMilli();
			request.setAttribute("startTime", startTime);
		}
		validation.clear();

		// username
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) authenticationToken.getCredentials();
		String username = (String) jwt.getClaims().get("preferred_username");
		if (username != null) {
			ThreadLocalStateUtil.setCurrentUser(username);
		}
		return true;
	}
}
