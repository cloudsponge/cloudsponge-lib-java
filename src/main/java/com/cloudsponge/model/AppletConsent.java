package com.cloudsponge.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object for the Desktop Applet import process. Renders the html code of the
 * applet needed to proceed with the import process.
 * 
 * @author andrenpaes
 */
public class AppletConsent extends ImportResponse {

	private static final long serialVersionUID = 1119039498197708190L;

	private final String appletSnippet;

	private String appletUrl;

	public AppletConsent() {
		this(null);
	}

	public AppletConsent(String appletSnippet) {
		this.appletSnippet = appletSnippet;
	}

	public String getAppletUrl() {
		return appletUrl;
	}

	public void setAppletUrl(String appletUrl) {
		this.appletUrl = appletUrl;
	}

	public String getAppletHtmlCode() {
		if (appletSnippet != null) {
			return appletSnippet.replaceAll("<%URL%>", appletUrl)
					.replaceAll("<%IMPORT_ID%>", getImportId());
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof AppletConsent && super.equals(obj)) {
			final AppletConsent that = (AppletConsent) obj;
			return new EqualsBuilder()
					.append(this.getAppletUrl(), that.getAppletUrl()).isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(9983, -911)
				.append(this.getImportId())
				.append(this.getAppletUrl()).toHashCode();
	}

	@Override
	protected ToStringBuilder toStringBuilder() {
		return super.toStringBuilder()
				.append("applet-url", this.getAppletUrl());
	}
}
