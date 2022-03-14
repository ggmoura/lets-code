package br.com.letscode.security.web;

import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true, jsr250Enabled = true)
public class SecurityAdapterConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfig jwtConfig;
	private final ObjectMapper mapper;

	public SecurityAdapterConfig(final JwtConfig jwtConfig, final ObjectMapper mapper) {
		this.jwtConfig = jwtConfig;
		this.mapper = mapper;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling().authenticationEntryPoint(unauthenticationEntryPoint()).and()
			.addFilterBefore(new JwtTokenAuthenticationFilter(jwtConfig, mapper), UsernamePasswordAuthenticationFilter.class)
			.authorizeRequests().antMatchers(jwtConfig.getFreeUrls()).permitAll().and()
			.authorizeRequests().anyRequest().authenticated().and()
			.authorizeRequests().and().headers().frameOptions().sameOrigin();
	}

	private AuthenticationEntryPoint unauthenticationEntryPoint() {
		return (req, rsp, exception) -> {
			rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			final String errorMessage = exception.getLocalizedMessage().concat(": ").concat(req.getServletPath());
			rsp.setContentType(MediaType.APPLICATION_JSON_VALUE);
			final ResponseMessage error = ResponseMessage.error(errorMessage);
			rsp.getWriter().append(mapper.writeValueAsString(ResponseObject.of(error)));
		};
	}

}