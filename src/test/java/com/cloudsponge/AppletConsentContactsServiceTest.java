package com.cloudsponge;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;

import com.cloudsponge.model.AppletConsent;

public class AppletConsentContactsServiceTest extends ContactsServiceTest<AppletConsent> {

	private AppletConsentContactsService contactsService;

	@Before
	public void setUp() throws Exception {
		contactsService = new AppletConsentContactsService(SERVICE_ID);
		cloudSpongeApi = new CloudSpongeApiImpl<AppletConsent>(
				contactsService, DOMAIN_KEY, DOMAIN_PASSWORD);
		cloudSpongeApi.setHttpService(httpService);
		cloudSpongeApi.setCloudSpongeParser(cloudSpongeParser);
	}

	@Test
	public void beginImport_applet_consent() {
		final AppletConsent expectedAppletConsent = new AppletConsent();
		expectedAppletConsent.setImportId("1");
		expectedAppletConsent.setAppletUrl("about:blank");

		final String contents = "response";
		when(httpService.execute((HttpRequestBase) any())).thenReturn(contents);
		when(cloudSpongeParser.parseAppletConsent(contents)).thenReturn(
				expectedAppletConsent);

		assertEquals(expectedAppletConsent,
				contactsService.beginImport(cloudSpongeApi));
		verify(httpService).createPost(
				"https://api.cloudsponge.com/begin_import/desktop_applet.xml", createFields());
		verify(cloudSpongeParser).parseAppletConsent(contents);
	}
}
