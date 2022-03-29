package br.com.letscode.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.text.MessageFormat;

@Component
public class TokenClientInterceptor implements RequestInterceptor {

	private Logger logger = LoggerFactory.getLogger(TokenClientInterceptor.class);

	@Override
	public void apply(RequestTemplate requestTemplate) {
		logger.info(MessageFormat.format("Feign Target, name: {0}, url: {1}",
			requestTemplate.feignTarget().name(),
			requestTemplate.feignTarget().url()
		));
	}

}
