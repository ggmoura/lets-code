package br.com.letscode.security.web;

import java.security.Principal;

public class UserPrincipal implements Principal {

	private String name;

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
