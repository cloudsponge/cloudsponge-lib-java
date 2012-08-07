package com.cloudsponge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;

import com.cloudsponge.model.UserConsent;

public class UserConsentContactsServiceTest extends ContactsServiceTest<UserConsent> {

	private UserConsentContactsService contactsService;

	@Before
	public void setUp() throws Exception {
		contactsService = new UserConsentContactsService(SERVICE_ID);
		cloudSpongeApi = new CloudSpongeApiImpl<UserConsent>(
				contactsService, DOMAIN_KEY, DOMAIN_PASSWORD);
		cloudSpongeApi.setHttpService(httpService);
		cloudSpongeApi.setCloudSpongeParser(cloudSpongeParser);
	}

	@Test
	public void beginImport_for_user_consent() {
		final UserConsent expectedUserConsent = new UserConsent();
		expectedUserConsent.setImportId("1");
		expectedUserConsent.setUrl("about:blank");

		final String contents = "response";
		when(httpService.execute((HttpRequestBase) any())).thenReturn(contents);
		when(cloudSpongeParser.parseUserConsent(contents)).thenReturn(
				expectedUserConsent);

		assertEquals(expectedUserConsent,
				contactsService.beginImport(cloudSpongeApi));
		verify(httpService).createPost(
				"https://api.cloudsponge.com/begin_import/user_consent.xml", createFields());
		verify(cloudSpongeParser).parseUserConsent(contents);
	}
}
