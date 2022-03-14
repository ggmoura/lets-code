package br.com.letscode.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AppPrincipal extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final UserPrincipal principal;

	public AppPrincipal(final UserPrincipal principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.principal = principal;
	}

	@Override
	public UserPrincipal getPrincipal() {
		return this.principal;
	}

}
