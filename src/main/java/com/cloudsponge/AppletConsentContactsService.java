package com.cloudsponge;

import org.apache.http.client.methods.HttpPost;

import com.cloudsponge.model.AppletConsent;

class AppletConsentContactsService extends ContactsService<AppletConsent> {

	private static final long serialVersionUID = -4313281598687201518L;

	public AppletConsentContactsService(String serviceId) {
		super(serviceId);
	}

	@Override
	public AppletConsent beginImport(
			CloudSpongeApiImpl<AppletConsent> cloudSponge) {
		final HttpPost post = createPost(
				CloudSpongeUrls.APPLET_CONSENT_IMPORT_URL, cloudSponge);

		final String response = executePost(post, cloudSponge);
		final CloudSpongeParser cloudSpongeParser =
				cloudSponge.getCloudSpongeParser();
		return cloudSpongeParser.parseAppletConsent(response);
	}
}
