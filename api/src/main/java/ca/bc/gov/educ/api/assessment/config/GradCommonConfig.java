package ca.bc.gov.educ.api.assessment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource(value = "classpath:messages.yaml", factory = YamlPropertySourceFactory.class)
public class GradCommonConfig implements WebMvcConfigurer {

	RequestResponseInterceptor requestInterceptor;

	@Autowired
	public GradCommonConfig(RequestResponseInterceptor requestInterceptor) {
		this.requestInterceptor = requestInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
	}

}
