package br.com.letscode.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	private static final String ZUP_URL = "https://www.letscode.com.br/";
	private static final String BEARER_SCHEME = "bearer";
	private static final String BEARER_FORMAT = "JWT";
	private static final String SECURITY_SCHEMES = "letscode-authorization-schemes";
	private static final String AUTHORIZATION_HEADER = "Authorization";

	@Bean
	public OpenAPI customOpenAPI(OpenApiAppParam params) {
		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEMES))
			.components(new Components().addSecuritySchemes(SECURITY_SCHEMES, securityScheme()))
			.info(new Info()
				.title(params.getDescription())
				.contact(contact())
				.version(params.getAppVersion())
				.description(params.getDescription())
				.termsOfService(ZUP_URL)
				.license(new License().name("Let's Code - 1.0").url(ZUP_URL))
			);
	}

	private Contact contact() {
		return new Contact().name("Let's Code").url(ZUP_URL).email("contact@letscode.com.br");
	}

	@Bean
	protected SecurityScheme securityScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(BEARER_SCHEME).bearerFormat(BEARER_FORMAT).in(SecurityScheme.In.HEADER).name(AUTHORIZATION_HEADER);
	}

}