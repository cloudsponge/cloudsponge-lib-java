package com.cloudsponge;

import org.apache.http.impl.client.DefaultHttpClient;

import com.cloudsponge.model.ImportResponse;

/**
 * Implementation for the {@link CloudSpongeApiFactoryImpl} class. This is where
 * you should put your Domain Key and Password.<br>
 * This class is thread-safe, thought the objects created by it are not.
 * 
 * @author andrenpaes
 */
public final class CloudSpongeApiFactoryImpl implements CloudSpongeApiFactory {

	private String domainKey;

	private String domainPassword;

	public CloudSpongeApiFactoryImpl() {
	}

	public CloudSpongeApiFactoryImpl(String domainKey, String domainPassword) {
		this.domainKey = domainKey;
		this.domainPassword = domainPassword;
	}

	/**
	 * Sets the domain key used for authentication with CloudSponge.
	 * @param domainKey
	 */
	public void setDomainKey(String domainKey) {
		this.domainKey = domainKey;
	}

	/**
	 * Sets the domain password used for authentication with CloudSponge.
	 * @param domainKey
	 */
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
