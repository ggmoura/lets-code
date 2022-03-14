package br.com.letscode.security.web;


import br.com.letscode.commons.ResponseMessage;
import br.com.letscode.commons.ResponseObject;
import br.com.letscode.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;
	private static final String ROLE_PREFIX = "ROLE_";
	private final ObjectMapper mapper;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig, final ObjectMapper mapper) {
		this.jwtConfig = jwtConfig;
		this.mapper = mapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
		try {
			String header = request.getHeader(jwtConfig.getHeader());
			if (header != null && header.startsWith(jwtConfig.getPrefix())) {
				final String token = header.replace(jwtConfig.getPrefix(), "");
				final Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token).getBody();
				final String edgeId = claims.getSubject();
				if (edgeId != null) {
					final List<String> privileges = new ArrayList<>();
					final List<String> roles = claims.get(jwtConfig.getClaimRolesField(), List.class);
					privileges.addAll(roles.stream().map(ROLE_PREFIX::concat).collect(Collectors.toList()));
					final List<SimpleGrantedAuthority> authorities = privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
					AppPrincipal auth = new AppPrincipal(getUserPrincipal(claims), null, authorities);
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			chain.doFilter(request, response);
		} catch (BusinessException e) {
			ResponseObject<Void> body = new ResponseObject<>();
			e.getErrors().forEach(body::addMessage);
			response.setStatus(e.getHttpStatus().value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(mapper.writeValueAsString(body));
		} catch (ExpiredJwtException e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(mapper.writeValueAsString(ResponseObject.of(ResponseMessage.error(e.getMessage()))));
		} catch (Exception e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(mapper.writeValueAsString(ResponseObject.of(ResponseMessage.error(e.getMessage()))));
		}
	}

	private UserPrincipal getUserPrincipal(Claims claims) {
		final UserPrincipal principal = new UserPrincipal();
		principal.setName(claims.get("name", String.class));
		principal.setUsername(claims.get("sub", String.class));
		return principal;
	}

}