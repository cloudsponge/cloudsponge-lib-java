package com.cloudsponge;

import java.util.Map;

import org.apache.http.client.methods.HttpPost;

import com.cloudsponge.model.ImportResponse;

class AuthenticationContactsService extends ContactsService<ImportResponse> {

	private static final long serialVersionUID = 2823188985592965384L;

	AuthenticationContactsService(String serviceId) {
		super(serviceId);
	}

	@Override
	public ImportResponse beginImport(CloudSpongeApiImpl<ImportResponse> cloudSponge) {
		final HttpPost post =
				createPost(CloudSpongeUrls.REGULAR_IMPORT_URL, cloudSponge);

		final String response = executePost(post, cloudSponge);
		final CloudSpongeParser cloudSpongeParser =
				cloudSponge.getCloudSpongeParser();
		return cloudSpongeParser.parseImportResponse(response);
	}

	@Override
	Map<String, String> createFormFields(CloudSpongeApiImpl<?> cloudSponge) {
		final Map<String, String> fields = super.createFormFields(cloudSponge);
		fields.put("username", cloudSponge.getUserName());
		fields.put("password", cloudSponge.getUserPassword());

		return fields;
	}
}
