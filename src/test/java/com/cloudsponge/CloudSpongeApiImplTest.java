package com.cloudsponge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Random;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.ProgressEvents;
import com.cloudsponge.model.UserContacts;

public class CloudSpongeApiImplTest {

	private static final String DOMAIN_KEY = "DOMAIN_KEY";

	private static final String DOMAIN_PASSWORD = "DOMAIN_PASSWORD";

	private CloudSpongeApiImpl<ImportResponse> cloudSpongeApi;

	@Mock
	private ContactsService<ImportResponse> contactsService;

	@Mock
	private CloudSpongeParser cloudSpongeParser;

	@Mock
	private CloudSpongeHttpServiceImpl httpService;

	private final String expectedImportId = "IMPORT_ID";

	private ImportResponse expectedImportResponse;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		cloudSpongeApi = new CloudSpongeApiImpl<ImportResponse>(
				contactsService, DOMAIN_KEY, DOMAIN_PASSWORD);
		cloudSpongeApi.setCloudSpongeParser(cloudSpongeParser);
		cloudSpongeApi.setHttpService(httpService);

		expectedImportResponse = new ImportResponse();
		expectedImportResponse.setImportId(expectedImportId);
		when(contactsService.beginImport(cloudSpongeApi)).thenReturn(
				expectedImportResponse);
	}

	private String configureHttpRequest() {
		final String responseContents = randomString();
		when(httpService.execute((HttpRequestBase) any())).thenReturn(responseContents);
		return responseContents;
	}

	private String randomString() {
		return "contents" + new Random().nextInt();
	}

	@Test
	public void testBeginImport() throws Exception {
		assertEquals(expectedImportResponse, cloudSpongeApi.beginImport());
		verify(contactsService).beginImport(cloudSpongeApi);
	}

	@Test
	public void testGetImportProgress() {
		final ProgressEvents expectedProgress = new ProgressEvents();
		expectedProgress.setImportId(expectedImportId);

		final String responseContents = configureHttpRequest();
		when(cloudSpongeParser.parseProgressEvents(responseContents))
				.thenReturn(expectedProgress);
		final String expectedUri =
				"https://api.cloudsponge.com/events.xml/" + expectedImportId;


		cloudSpongeApi.beginImport();
		assertEquals(expectedProgress, cloudSpongeApi.getImportProgress());

		verify(contactsService).beginImport(cloudSpongeApi);
		verify(httpService).createGet(expectedUri);
		verify(httpService).addAuthenticationCredentials(
				(HttpRequestBase) any(), eq(DOMAIN_KEY), eq(DOMAIN_PASSWORD));
		verify(cloudSpongeParser).parseProgressEvents(responseContents);
	}

	@Test
	public void testGetImportProgress_With_Optional_Parameters() {
		final ProgressEvents expectedProgress = new ProgressEvents();
		expectedProgress.setImportId(expectedImportId);

		String echo = randomString();
		String userId = randomString();
		cloudSpongeApi.echo(echo).userId(userId);

		final String responseContents = configureHttpRequest();
		when(cloudSpongeParser.parseProgressEvents(responseContents))
			.thenReturn(expectedProgress);
		final String expectedUri =
				"https://api.cloudsponge.com/events.xml/" + expectedImportId;


		cloudSpongeApi.beginImport();
		assertEquals(expectedProgress, cloudSpongeApi.getImportProgress());

		verify(contactsService).beginImport(cloudSpongeApi);
		verify(httpService).createGet(expectedUri);
		verify(httpService).addAuthenticationCredentials(
				(HttpRequestBase) any(), eq(DOMAIN_KEY), eq(DOMAIN_PASSWORD));
		verify(httpService).addParameter((HttpRequestBase) any(), eq("user_id"), eq(userId));
		verify(httpService).addParameter((HttpRequestBase) any(), eq("echo"), eq(echo));
		verify(cloudSpongeParser).parseProgressEvents(responseContents);
	}

	@Test
	public void testFetchContacts() {
		final UserContacts expectedContacts = new UserContacts();
		expectedContacts.setImportId(expectedImportId);

		final String responseContents = configureHttpRequest();
		when(cloudSpongeParser.parseContacts(responseContents)).thenReturn(
				expectedContacts);
		final String expectedUri =
				"https://api.cloudsponge.com/contacts.xml/" + expectedImportId;

		cloudSpongeApi.beginImport();
		final UserContacts actualContacts = cloudSpongeApi.fetchContacts();
		verify(contactsService).beginImport(cloudSpongeApi);
		verify(httpService).createGet(expectedUri);
		verify(httpService).addAuthenticationCredentials(
				(HttpRequestBase) any(), eq(DOMAIN_KEY), eq(DOMAIN_PASSWORD));
		verify(cloudSpongeParser).parseContacts(responseContents);
		assertEquals(expectedContacts, actualContacts);
	}

	@Test
	public void testClose() throws Exception {
		cloudSpongeApi.close();
		verify(httpService).close();
	}
}
