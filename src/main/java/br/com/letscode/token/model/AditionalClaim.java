package br.com.letscode.token.model;

public class AditionalClaim {

	private String claimName;
	private String claimValue;
	private Boolean applyInRefreshToken;

	public AditionalClaim(String claimName, String claimValue, Boolean applyInRefreshToken) {
		this();
		this.claimName = claimName;
		this.claimValue = claimValue;
		this.applyInRefreshToken = applyInRefreshToken;
	}

	public AditionalClaim() {
		super();
	}

	public String getClaimName() {
		return claimName;
	}

	public void setClaimName(String claimName) {
		this.claimName = claimName;
	}

	public String getClaimValue() {
		return claimValue;
	}

	public void setClaimValue(String claimValue) {
		this.claimValue = claimValue;
	}

	public Boolean getApplyInRefreshToken() {
		return applyInRefreshToken;
	}

	public void setApplyInRefreshToken(Boolean applyInRefreshToken) {
		this.applyInRefreshToken = applyInRefreshToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((claimName == null) ? 0 : claimName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && getClass() == obj.getClass()) {
			AditionalClaim other = (AditionalClaim) obj;
			if (claimName != null && other.claimName != null) {
				equals = claimName.equals(other.claimName);
			} else if (claimName == null && other.claimName == null) {
				equals = true;
			}
		}
		return equals;
	}

}
