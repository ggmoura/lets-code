package br.com.letscode.controller.model.user;

import javax.validation.constraints.NotNull;


public abstract class UserDTO {

	@NotNull
	private String username;

	@NotNull
	private String name;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}