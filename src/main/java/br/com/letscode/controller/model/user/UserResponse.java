package br.com.letscode.controller.model.user;

import br.com.letscode.token.entity.Privilege;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserResponse extends UserDTO {

	private List<Privilege> privileges;

	public List<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Privilege> privileges) {
		this.privileges = privileges;
	}
}
