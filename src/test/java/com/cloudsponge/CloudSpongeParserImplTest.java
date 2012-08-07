package com.cloudsponge;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.cloudsponge.model.AppletConsent;
import com.cloudsponge.model.Contact;
import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.ProgressEvents;
import com.cloudsponge.model.UserConsent;
import com.cloudsponge.model.UserContacts;
import com.cloudsponge.model.Contact.Email;
import com.cloudsponge.model.Contact.PhoneNumber;
import com.cloudsponge.model.ImportResponse.ImportStatus;
import com.cloudsponge.model.ProgressEvents.Event;
import com.cloudsponge.model.ProgressEvents.EventStatus;
import com.cloudsponge.model.ProgressEvents.EventType;

public class CloudSpongeParserImplTest {

	private CloudSpongeParserImpl cloudSpongeParser;

	@Before
	public void setUp() throws Exception {
		cloudSpongeParser = new CloudSpongeParserImpl();
	}

	private String loadResource(String resource) {
		try {
			return IOUtils.toString(getClass().getResourceAsStream(resource), "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test(expected = CloudSpongeParserException.class)
	public void testParseImportResponse_malformed_xml() {
		final ImportResponse expectedImportResponse = new ImportResponse();
		expectedImportResponse.setImportId("1131");
		expectedImportResponse.setStatus(ImportStatus.SUCCESS);

		final String xmlContents = loadResource("/xmls/malformed.xml");
		cloudSpongeParser.parseImportResponse(xmlContents);
	}

	@Test
	public void testParseImportResponseSuccess() {
		final ImportResponse expectedImportResponse = new ImportResponse();
		expectedImportResponse.setImportId("1131");
		expectedImportResponse.setStatus(ImportStatus.SUCCESS);

		final String xmlContents = loadResource("/xmls/import.xml");
		final ImportResponse importResponse = cloudSpongeParser
				.parseImportResponse(xmlContents);

		assertEquals(expectedImportResponse, importResponse);
	}

	@Test
	public void testParseImportResponseError() {
		final ImportResponse expectedUserConsent = new ImportResponse();
		expectedUserConsent.setImportId("1131");
		expectedUserConsent.setStatus(ImportStatus.ERROR);
		expectedUserConsent.setErrorMessage("Error!");

		final String xmlContents = loadResource("/xmls/import_error.xml");
		final ImportResponse importResponse = cloudSpongeParser
				.parseImportResponse(xmlContents);

		assertEquals(expectedUserConsent, importResponse);
	}

	@Test
	public void testParseUserConsent() {
		final UserConsent expectedUserConsent = new UserConsent();
		expectedUserConsent.setImportId("1126");
		expectedUserConsent.setStatus(ImportStatus.SUCCESS);
		expectedUserConsent
				.setUrl("https://api.login.yahoo.com/oauth/v2/request_auth?oauth_token=d");
		expectedUserConsent.setEcho("echo123");
		expectedUserConsent.setUserId("myUserId_0003");

		final String xmlContents = loadResource("/xmls/user_consent.xml");
		final UserConsent userConsent = cloudSpongeParser
				.parseUserConsent(xmlContents);

		assertEquals(expectedUserConsent, userConsent);
	}

	@Test
	public void testParseAppletConsent() {
		final AppletConsent expectedAppletConsent = new AppletConsent("");
		expectedAppletConsent.setImportId("1132");
		expectedAppletConsent.setStatus(ImportStatus.SUCCESS);
		expectedAppletConsent
				.setAppletUrl("https://api.cloudsponge.com/objects/ContactsApplet_signed.jar");
		expectedAppletConsent.setEcho("Hello?");

		final String xmlContents = loadResource("/xmls/applet_consent.xml");
		final AppletConsent appletConsent = cloudSpongeParser
				.parseAppletConsent(xmlContents);

		assertEquals(expectedAppletConsent, appletConsent);
	}

	@Test
	public void testParseProgressEvents() {
		final ProgressEvents expectedProgressEvents = new ProgressEvents();
		expectedProgressEvents.setImportId("1128");

		final Event initializingEvent = new Event();
		initializingEvent.setType(EventType.INITIALIZING);
		initializingEvent.setStatus(EventStatus.COMPLETED);
		initializingEvent.setValue(0);
		expectedProgressEvents.addEvent(initializingEvent);

		final Event gatheringEvent = new Event();
		gatheringEvent.setType(EventType.GATHERING);
		gatheringEvent.setStatus(EventStatus.COMPLETED);
		gatheringEvent.setValue(2);
		expectedProgressEvents.addEvent(gatheringEvent);

		final Event completeEvent = new Event();
		completeEvent.setType(EventType.COMPLETE);
		completeEvent.setStatus(EventStatus.COMPLETED);
		completeEvent.setValue(0);
		expectedProgressEvents.addEvent(completeEvent);

		final String xmlContents = loadResource("/xmls/progress_events.xml");
		final ProgressEvents progressEvents = cloudSpongeParser
				.parseProgressEvents(xmlContents);

		assertEquals(expectedProgressEvents, progressEvents);
	}

	@Test
	public void testParseContacts() {
		final UserContacts expectedUserContacts = new UserContacts();
		expectedUserContacts.setImportId("1126");
		expectedUserContacts.setUserId("myUserId_0003");

		final Contact owner = new Contact();
		owner.setFirstName("Joe");
		owner.setLastName("Smith");
		owner.addEmail(new Email("joe@example.com"));
		expectedUserContacts.setOwner(owner);

		final Contact john = new Contact();
		john.setFirstName("John");
		john.setLastName("Doe");
		john.addEmail(new Email("johndoe@nowhere.com", "Email 1"));
		john.addEmail(new Email("second@email.com", "Email 2"));
		john.addPhoneNumber(new PhoneNumber("555-1234", "Home"));
		john.addPhoneNumber(new PhoneNumber("555-2468", "Work"));
		expectedUserContacts.addContact(john);

		final Contact jane = new Contact();
		jane.setFirstName("Jane");
		jane.setLastName("Smith");
		jane.addEmail(new Email("janesmith@nowhere.com", "Email 1"));
		jane.addPhoneNumber(new PhoneNumber("555.5678", "Home"));
		expectedUserContacts.addContact(jane);

		final String xmlContents = loadResource("/xmls/user_contacts.xml");
		final UserContacts userContacts = cloudSpongeParser.parseContacts(xmlContents);
		assertEquals(expectedUserContacts, userContacts);
		assertEquals(expectedUserContacts.getContacts(),
				userContacts.getContacts());
	}
}
