package com.cloudsponge;

public final class CloudSpongeUrls {

	private static final String HOST_URL = "https://api.cloudsponge.com";

	public static final String USER_CONSENT_IMPORT_URL = HOST_URL
			+ "/begin_import/user_consent.xml";

	public static final String REGULAR_IMPORT_URL = HOST_URL
			+ "/begin_import/import.xml";

	public static final String APPLET_CONSENT_IMPORT_URL = HOST_URL
			+ "/begin_import/desktop_applet.xml";

	public static final String PROGRESS_EVENTS_URL = HOST_URL + "/events.xml/";

	public static final String CONTACTS_URL = HOST_URL + "/contacts.xml/";

	public static String progressEventsUrl(String importId) {
		return PROGRESS_EVENTS_URL + importId;
	}

	public static String contactsUrl(String importId) {
		return CONTACTS_URL + importId;
	}
}
