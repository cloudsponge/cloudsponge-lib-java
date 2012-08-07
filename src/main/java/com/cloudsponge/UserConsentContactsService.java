package com.cloudsponge;

import org.apache.http.client.methods.HttpPost;

import com.cloudsponge.model.UserConsent;

class UserConsentContactsService extends ContactsService<UserConsent> {

	private static final long serialVersionUID = 4799375143986753470L;

	UserConsentContactsService(String serviceId) {
		super(serviceId);
	}

	@Override
	public UserConsent beginImport(CloudSpongeApiImpl<UserConsent> cloudSponge) {
		final HttpPost post = createPost(
				CloudSpongeUrls.USER_CONSENT_IMPORT_URL, cloudSponge);

		final String response = executePost(post, cloudSponge);
		final CloudSpongeParser cloudSpongeParser = cloudSponge
				.getCloudSpongeParser();
		return cloudSpongeParser.parseUserConsent(response);
	}
}
