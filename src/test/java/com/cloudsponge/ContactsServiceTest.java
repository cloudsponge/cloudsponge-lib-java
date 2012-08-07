package com.cloudsponge;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cloudsponge.model.ImportResponse;

@RunWith(MockitoJUnitRunner.class)
public abstract class ContactsServiceTest<T extends ImportResponse> {

	protected static final String SERVICE_ID = "SERVICE_ID";

	protected static final String DOMAIN_KEY = "DOMAIN_KEY";

	protected static final String DOMAIN_PASSWORD = "DOMAIN_PASSWORD";

	protected CloudSpongeApiImpl<T> cloudSpongeApi;

	@Mock
	protected CloudSpongeHttpService httpService;
	@Mock
	protected CloudSpongeParser cloudSpongeParser;

	protected String userId;

	protected String echo;

	public ContactsServiceTest() {
	}

	@Before
	public final void cleanFields() {
		userId = null;
		echo = null;
	}

	protected Map<String, String> createFields() {
		final Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("service", SERVICE_ID);
		fields.put("domain_key", DOMAIN_KEY);
		fields.put("domain_password", DOMAIN_PASSWORD);
		if (StringUtils.isNotEmpty(userId)) {
			fields.put("user_id", userId);
		}
		if (StringUtils.isNotEmpty(echo)) {
			fields.put("echo", echo);
		}

		return fields;
	}
}
