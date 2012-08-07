package com.cloudsponge;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.cloudsponge.model.ImportResponse;

public class AuthenticationContactsServiceTest extends
		ContactsServiceTest<ImportResponse> {

	private AuthenticationContactsService contactsService;

	/* Fields for HTTP Post Parameters */

	String userName;

	String userPassword;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		contactsService = new AuthenticationContactsService(SERVICE_ID);
		cloudSpongeApi = new CloudSpongeApiImpl<ImportResponse>(
				contactsService, DOMAIN_KEY, DOMAIN_PASSWORD);
		cloudSpongeApi.setHttpService(httpService);
		cloudSpongeApi.setCloudSpongeParser(cloudSpongeParser);

		userName = null;
		userPassword = null;
		userId = null;
		echo = null;
	}

	@Test
	public void beginImport_with_user_authentication() {
		userName = "user";
		userPassword = "passwd";
		cloudSpongeApi.userName(userName).userPassword(userPassword);

		final ImportResponse expectedImportResponse = new ImportResponse();
		expectedImportResponse.setImportId("1");

		final String contents = "response";
		when(httpService.execute((HttpRequestBase) any())).thenReturn(contents);
		when(cloudSpongeParser.parseImportResponse(contents)).thenReturn(
				expectedImportResponse);

		assertEquals(expectedImportResponse,
				contactsService.beginImport(cloudSpongeApi));
		verify(httpService).createPost(
				"https://api.cloudsponge.com/begin_import/import.xml", createFields());
	}

	@Test
	public void beginImport_with_user_authentication_and_userId_and_echo() {
		userName = "user";
		userPassword = "passwd";
		userId = "user1";
		echo = "echo";
		cloudSpongeApi.userName(userName).userPassword(userPassword)
				.userId(userId).echo(echo);
		
		final ImportResponse expectedImportResponse = new ImportResponse();
		expectedImportResponse.setImportId("1");
		
		final String contents = "response";
		when(httpService.execute((HttpRequestBase) any())).thenReturn(contents);
		when(cloudSpongeParser.parseImportResponse(contents)).thenReturn(
				expectedImportResponse);

		assertEquals(expectedImportResponse,
				contactsService.beginImport(cloudSpongeApi));
		verify(httpService).createPost(
				"https://api.cloudsponge.com/begin_import/import.xml", createFields());
	}

	 @Override
	protected Map<String, String> createFields() {
		Map<String, String> fields = super.createFields();
		fields.put("username", userName);
		fields.put("password", userPassword);
		return fields;
	}
}
