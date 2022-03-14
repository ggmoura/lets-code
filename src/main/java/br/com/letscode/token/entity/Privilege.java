package br.com.letscode.token.entity;

public enum Privilege {

	PLAYER("movies.battle.player"),
	MANAGER("movies.battle.manager");

	private String role;

	private Privilege(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}
}