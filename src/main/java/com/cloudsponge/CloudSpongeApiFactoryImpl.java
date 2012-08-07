package com.cloudsponge;

import org.apache.http.impl.client.DefaultHttpClient;

import com.cloudsponge.model.ImportResponse;

public final class CloudSpongeApiFactoryImpl implements CloudSpongeApiFactory {
	private String domainKey;

	private String domainPassword;

	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}

	public void setDomainPassword(String domainPassword) {
		this.domainPassword = domainPassword;
	}

	@Override
	public <T extends ImportResponse> CloudSpongeApi<T> create(
			ContactsService<T> contactsService) {
		final CloudSpongeApiImpl<T> cloudSponge = new CloudSpongeApiImpl<T>(
				contactsService, domainKey, domainPassword);

		final CloudSpongeParser cloudSpongeParser = new CloudSpongeParserImpl();
		cloudSponge.setCloudSpongeParser(cloudSpongeParser);

		final CloudSpongeHttpService httpService =
				new CloudSpongeHttpServiceImpl(new DefaultHttpClient());
		cloudSponge.setHttpService(httpService);

		return cloudSponge;
	}
}
