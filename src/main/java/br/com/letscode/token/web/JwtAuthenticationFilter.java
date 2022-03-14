package br.com.letscode.token.web;

import br.com.letscode.commons.ResponseObject;
import br.com.letscode.exception.BusinessException;
import br.com.letscode.security.web.JwtConfig;
import br.com.letscode.token.entity.User;
import br.com.letscode.token.model.AditionalClaim;
import br.com.letscode.token.model.ResponseToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authManager;
	private final ObjectMapper mapper;
	private final JwtConfig jwtConfig;

	public JwtAuthenticationFilter(final AuthenticationManager authManager, final JwtConfig jwtConfig, final ObjectMapper mapper) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		this.mapper = mapper;
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), HttpMethod.POST.name()));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
		Authentication authenticate = authManager.authenticate(authToken);
		return authenticate;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {

		final AppUserDetails userDetails = (AppUserDetails) auth.getPrincipal();
		final User user = userDetails.getUser();

		ZonedDateTime now = ZonedDateTime.now();
		final JwtBuilder builder = Jwts.builder().setSubject(auth.getName());
		builder.setIssuedAt(Date.from(now.toInstant())).setExpiration(Date.from(now.plusMinutes(jwtConfig.getExpiration() * 1000).toInstant()));
		builder.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes());
		response.setCharacterEncoding("UTF-8");
		try {
			final Set<AditionalClaim> handler = new HashSet<>();
			handler.stream().filter(AditionalClaim::getApplyInRefreshToken).forEach(claim -> builder.claim(claim.getClaimName(), claim.getClaimValue()));
			String refreshToken = builder.compact();
			handler.stream().filter(claim -> !claim.getApplyInRefreshToken()).forEach(claim -> builder.claim(claim.getClaimName(), claim.getClaimValue()));
			builder.claim("name", user.getName());
			builder.claim(jwtConfig.getClaimRolesField(), auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
			builder.setExpiration(Date.from(now.plusMinutes(jwtConfig.getExpiration()).toInstant()));
			String token = builder.compact();
			ResponseToken responseToken = new ResponseToken(token, refreshToken);
			response.getWriter().write(mapper.writeValueAsString(responseToken));
		} catch (BusinessException e) {
			response.setStatus(e.getHttpStatus().value());
			response.getWriter().write(mapper.writeValueAsString(ResponseObject.message(e.getErrors())));
		};
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().flush();
	}

}