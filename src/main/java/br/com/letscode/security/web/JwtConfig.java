package br.com.letscode.security.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class JwtConfig {

	private String [] internalUrls = {
		"/favicon.ico",
		"/auth/**",
		"/h2/**",
		"/h2-console/**",
		"/v3/api-docs/**",
		"/configuration/ui",
		"/swagger-resources/**",
		"/configuration/security",
		"/swagger-ui.html",
		"/swagger.yaml",
		"/swagger-ui/**",
		"/webjars/**",
		"/actuator/**",
		"/error"
	};

	@Value("${security.jwt.uri:/auth/**}")
	private String uri;

	@Value("${security.jwt.header:Authorization}")
	private String header;

	@Value("${security.jwt.prefix:Bearer }")
	private String prefix;

	@Value("${security.jwt.expiration:#{3600}}")
	private int expiration;

	@Value("${security.jwt.secret:dmFsZW50aW5h}")
	private String secret;

	@Value("${security.public.url:}")
	private String[] publicUrls;

	@Value("${spring.profiles.active:null}")
	private String activeProfiles;

	@Value("${spring.profiles.claimrolesfield:authorities}")
	private String claimRolesField;

	public String getUri() {
		return uri;
	}

	public String[] getInternalUrls() {
		return internalUrls;
	}

	public String getHeader() {
		return header;
	}

	public String getPrefix() {
		return prefix;
	}

	public int getExpiration() {
		return expiration;
	}

	public String getSecret() {
		return secret;
	}

	public String[] getPublicUrls() {
		return publicUrls;
	}

	public String getActiveProfiles() {
		return activeProfiles;
	}

	public String getClaimRolesField() {
		return claimRolesField;
	}

	public String[] getFreeUrls() {
		final String[] publics = getPublicUrls();
		final String[] internals = getInternalUrls();
		return Stream.of(publics, internals).flatMap(Stream::of).toArray(String[]::new);
	}

}
