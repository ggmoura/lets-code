package br.com.letscode.controller.model.user;


import javax.validation.constraints.NotNull;

public class UserRequest extends UserDTO {

	@NotNull
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
