package com.cloudsponge.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Base response for every CloudSponge request. Contains the echo and user-id
 * strings along with the import-id.
 * 
 * @author andrenpaes
 * 
 */
public abstract class CloudSpongeResponse implements Serializable {

	private static final long serialVersionUID = 3345090496290102658L;

	private String importId;

	private String userId;

	private String echo;

	public CloudSpongeResponse() {
	}

	public String getImportId() {
		return importId;
	}

	public void setImportId(String importId) {
		this.importId = importId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEcho() {
		return echo;
	}

	public void setEcho(String echo) {
		this.echo = echo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof CloudSpongeResponse) {
			final CloudSpongeResponse that = (CloudSpongeResponse) obj;
			return new EqualsBuilder()
					.append(this.getImportId(), that.getImportId())
					.append(this.getUserId(), that.getUserId())
					.append(this.getEcho(), that.getEcho()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(-1, -11111).append(this.getImportId())
				.append(this.getUserId()).append(this.getEcho()).toHashCode();
	}

	@Override
	public String toString() {
		return toStringBuilder().toString();
	}

	protected ToStringBuilder toStringBuilder() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("import-id", this.getImportId())
				.append("user-id", this.getUserId())
				.append("echo", this.getEcho());
	}
}
