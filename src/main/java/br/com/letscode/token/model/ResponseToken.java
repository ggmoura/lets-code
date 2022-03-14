package br.com.letscode.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseToken {

	@JsonProperty("refresh-token")
	private String refreshToken;
	private String token;

	public ResponseToken() {
		super();
	}

	public ResponseToken(String token, String refreshToken) {
		this();
		this.token = token;
		this.refreshToken = refreshToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
