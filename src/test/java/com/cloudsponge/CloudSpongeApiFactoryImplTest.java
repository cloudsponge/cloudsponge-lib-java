package com.cloudsponge;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cloudsponge.model.UserConsent;

public class CloudSpongeApiFactoryImplTest {

	@Test
	public void create() {
		final String domainKey = "key";
		final String domainPassword = "pass";

		final CloudSpongeApiFactoryImpl factory = new CloudSpongeApiFactoryImpl();
		factory.setDomainKey(domainKey);
		factory.setDomainPassword(domainPassword);

		final CloudSpongeApiImpl<UserConsent> cloudSponge =
				(CloudSpongeApiImpl<UserConsent>) factory.create(ContactsService.GMAIL);
		assertEquals(domainKey, cloudSponge.getDomainKey());
		assertEquals(domainPassword, cloudSponge.getDomainPassword());
		assertNotNull(cloudSponge.getCloudSpongeParser());
		assertNotNull(cloudSponge.getHttpService());
	}
}
