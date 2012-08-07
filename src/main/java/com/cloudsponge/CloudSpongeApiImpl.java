package com.cloudsponge;

import org.apache.http.client.methods.HttpGet;

import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.ProgressEvents;
import com.cloudsponge.model.UserContacts;

class CloudSpongeApiImpl<T extends ImportResponse> implements
		CloudSpongeApi<T> {

	private CloudSpongeHttpService httpService;

	private CloudSpongeParser cloudSpongeParser;

	private final ContactsService<T> contactsService;

	private final String domainKey;

	private final String domainPassword;

	private String userName;

	private String userPassword;

	private String userId;

	private String echo;

	private String importId;

	CloudSpongeApiImpl(ContactsService<T> contactsService, String domainKey,
			String domainPassword) {
		this.contactsService = contactsService;
		this.domainKey = domainKey;
		this.domainPassword = domainPassword;
	}

	CloudSpongeHttpService getHttpService() {
		return httpService;
	}

	void setHttpService(CloudSpongeHttpService httpService) {
		this.httpService = httpService;
	}

	CloudSpongeParser getCloudSpongeParser() {
		return cloudSpongeParser;
	}

	void setCloudSpongeParser(CloudSpongeParser cloudSpongeParser) {
		this.cloudSpongeParser = cloudSpongeParser;
	}

	String getDomainKey() {
		return domainKey;
	}

	String getDomainPassword() {
		return domainPassword;
	}

	@Override
	public CloudSpongeApi<T> userName(String userName) {
		this.userName = userName;
		return this;
	}

	@Override
	public String getUserName() {
		return this.userName;
	}

	@Override
	public CloudSpongeApi<T> userPassword(String userPassword) {
		this.userPassword = userPassword;
		return this;
	}

	@Override
	public String getUserPassword() {
		return this.userPassword;
	}

	@Override
	public CloudSpongeApi<T> userId(String userId) {
		this.userId = userId;
		return this;
	}

	@Override
	public String getUserId() {
		return this.userId;
	}

	@Override
	public CloudSpongeApi<T> echo(String echo) {
		this.echo = echo;
		return this;
	}

	@Override
	public String getEcho() {
		return this.echo;
	}

	@Override
	public T beginImport() {
		final T importResponse = contactsService.beginImport(this);
		this.importId = importResponse.getImportId();

		return importResponse;
	}

	@Override
	public ProgressEvents getImportProgress() {
		final HttpGet get = httpService.createGet(CloudSpongeUrls.progressEventsUrl(importId));
		addAuthenticationCredentials(get);
		addOptionalParameters(get);

		return cloudSpongeParser.parseProgressEvents(httpService.execute(get));
	}

	@Override
	public UserContacts fetchContacts() {
		final HttpGet get = httpService.createGet(CloudSpongeUrls.contactsUrl(importId));
		addAuthenticationCredentials(get);
		addOptionalParameters(get);

		return cloudSpongeParser.parseContacts(httpService.execute(get));
	}

	@Override
	public void close() {
		httpService.close();
	}

	private void addAuthenticationCredentials(HttpGet get) {
		httpService.addAuthenticationCredentials(get, getDomainKey(), getDomainPassword());
	}

	private void addOptionalParameters(HttpGet get) {
		httpService.addParameter(get, "user_id", getUserId());
		httpService.addParameter(get, "echo", getEcho());
	}
}
