package com.cloudsponge.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object for the Consent Page import process. Contains the URL that needs to
 * accessed for authorizing the import process.
 * 
 * @author andrenpaes
 * 
 */
public class UserConsent extends ImportResponse {

	private static final long serialVersionUID = 5542094612652810253L;

	private String url;

	public UserConsent() {
	}

	/**
	 * Consent page URL.
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof UserConsent && super.equals(obj)) {
			final UserConsent that = (UserConsent) obj;
			return new EqualsBuilder().append(this.getUrl(), that.getUrl())
					.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(-12121, 122177).append(this.getUrl())
				.toHashCode();
	}

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("url", this.getUrl());
	}
}
