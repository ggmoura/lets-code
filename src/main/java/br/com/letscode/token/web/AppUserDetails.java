package br.com.letscode.token.web;


import br.com.letscode.token.entity.Privilege;
import br.com.letscode.token.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.*;

public class AppUserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 1L;

	private final User user;
	private Map<String, String> aditionalFields;

	public AppUserDetails(User user) {
		super(user.getUsername(), user.getPassword(), getAuthorities(user.getPrivileges()));
		this.user = user;
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(List<Privilege> privileges) {
		List<String> authorities = new ArrayList<>();
		privileges.forEach(privilege -> authorities.add(privilege.name()));
		return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authorities));
	}

	public User getUser() {
		return user;
	}

	public Map<String, String> getAditionalFields() {
		return aditionalFields;
	}

	public void setAditionalFields(Map<String, String> aditionalFields) {
		this.aditionalFields = aditionalFields;
	}

}
