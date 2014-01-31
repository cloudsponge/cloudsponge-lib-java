package com.cloudsponge;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;

import com.cloudsponge.model.AppletConsent;
import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.UserConsent;

/**
 * Lists the available sources of contacts.
 * @author andrenpaes
 *
 * @param <T> type of the object used to proceed with the import process
 * @see UserConsent
 * @see AppletConsent
 * @see ImportResponse
 */
public abstract class ContactsService<T extends ImportResponse> implements
		Serializable {

	private static final long serialVersionUID = -3172373804147826532L;

	public static final ContactsService<UserConsent> GMAIL =
			new UserConsentContactsService("GMAIL");

	public static final ContactsService<UserConsent> WINDOWS_LIVE =
			new UserConsentContactsService("WINDOWSLIVE");

	public static final ContactsService<UserConsent> YAHOO =
			new UserConsentContactsService("YAHOO");

	public static final ContactsService<UserConsent> AOL =
			new UserConsentContactsService("AOL");

	public static final ContactsService<ImportResponse> PLAXO =
			new AuthenticationContactsService("PLAXO");

	public static final ContactsService<AppletConsent> MAC_ADDRESS_BOOK =
			new AppletConsentContactsService("ADDRESSBOOK");

	public static final ContactsService<AppletConsent> OUTLOOK =
			new AppletConsentContactsService("OUTLOOK");

	private final String serviceId;

	ContactsService(String serviceId) {
		this.serviceId = serviceId;
	}

	public final String getServiceId() {
		return serviceId;
	}

	abstract T beginImport(CloudSpongeApiImpl<T> cloudSponge);

	final HttpPost createPost(String url, CloudSpongeApiImpl<T> cloudSponge) {
		return cloudSponge.getHttpService().createPost(url, createFormFields(cloudSponge));
	}

	/**
	 * Extension point for each implementation of {@link ContactsService} to add
	 * their own fields to the post request.
	 * @param cloudSponge the api
	 * @return form fields
	 */
	Map<String, String> createFormFields(CloudSpongeApiImpl<?> cloudSponge) {
		Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("service", getServiceId());
		fields.put("domain_key", cloudSponge.getDomainKey());
		fields.put("domain_password", cloudSponge.getDomainPassword());

		if (StringUtils.isNotEmpty(cloudSponge.getUserId())) {
			fields.put("user_id", cloudSponge.getUserId());
		}
		if (StringUtils.isNotEmpty(cloudSponge.getEcho())) {
			fields.put("echo", cloudSponge.getEcho());
		}

		return fields;
	}

	final String executePost(HttpPost post, CloudSpongeApiImpl<?> cloudSponge) {
		return cloudSponge.getHttpService().execute(post);
	}
}
