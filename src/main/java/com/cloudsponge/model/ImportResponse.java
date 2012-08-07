package com.cloudsponge.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object for the regular import process (username and password).
 * @author andrenpaes
 */
public class ImportResponse extends CloudSpongeResponse {

	private static final long serialVersionUID = 8713318679770631427L;

	public enum ImportStatus {
		SUCCESS, ERROR;

		private static ImportStatus toStatus(String status) {
			for (ImportStatus importStatus : values()) {
				if (importStatus.name().equalsIgnoreCase(status)) {
					return importStatus;
				}
			}
			return null;
		}
	}

	private ImportStatus status;

	private String errorMessage;

	public ImportStatus getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = ImportStatus.toStatus(status);
	}

	public void setStatus(ImportStatus status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Indicates if the import process has successfully begun.
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean isSucessful() {
		return status == ImportStatus.SUCCESS;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof ImportResponse && super.equals(obj)) {
			final ImportResponse that = (ImportResponse) obj;
			return new EqualsBuilder()
					.append(this.getStatus(), that.getStatus())
					.append(this.getErrorMessage(), that.getErrorMessage())
					.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(115687, 7).append(this.getStatus())
				.append(this.getErrorMessage()).toHashCode();
	}

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder().append("status", getStatus())
				.append("error-message", this.getErrorMessage());
	}
}
