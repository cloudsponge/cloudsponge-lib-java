package com.cloudsponge;

import com.cloudsponge.model.AppletConsent;
import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.ProgressEvents;
import com.cloudsponge.model.UserConsent;
import com.cloudsponge.model.UserContacts;

/**
 * Parser for all the entities returned by the CloudSponge Service.
 * @author andrenpaes
 */
interface CloudSpongeParser {

	ImportResponse parseImportResponse(String contents)
			throws CloudSpongeParserException;

	UserConsent parseUserConsent(String contents)
			throws CloudSpongeParserException;

	AppletConsent parseAppletConsent(String contents)
			throws CloudSpongeParserException;

	ProgressEvents parseProgressEvents(String contents)
			throws CloudSpongeParserException;

	UserContacts parseContacts(String contents)
			throws CloudSpongeParserException;
}
