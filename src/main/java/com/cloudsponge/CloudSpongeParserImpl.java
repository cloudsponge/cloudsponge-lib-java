package com.cloudsponge;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cloudsponge.model.AppletConsent;
import com.cloudsponge.model.CloudSpongeResponse;
import com.cloudsponge.model.Contact;
import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.ProgressEvents;
import com.cloudsponge.model.UserConsent;
import com.cloudsponge.model.UserContacts;
import com.cloudsponge.model.Contact.Email;
import com.cloudsponge.model.Contact.PhoneNumber;
import com.cloudsponge.model.ProgressEvents.Event;

class CloudSpongeParserImpl implements CloudSpongeParser {

	private static final String appletSnippet = readResource(
			"/com/cloudsponge/applet.snippet", "UTF-8");

	private final DocumentBuilderFactory documentBuilderFactory =
			DocumentBuilderFactory.newInstance();

	private final XPath xPath = XPathFactory.newInstance().newXPath();

	@Override
	public ImportResponse parseImportResponse(String contents) {
		final ImportResponse importResponse = new ImportResponse();
		parseImportResponse(importResponse, getDocumentRoot(contents));

		return importResponse;
	}

	@Override
	public UserConsent parseUserConsent(String contents) {
		final UserConsent userConsent = new UserConsent();
		parseUserConsent(userConsent, getDocumentRoot(contents));

		return userConsent;
	}

	@Override
	public AppletConsent parseAppletConsent(String contents) {
		final AppletConsent appletConsent = new AppletConsent(
				CloudSpongeParserImpl.appletSnippet);
		parseAppletConsent(appletConsent, getDocumentRoot(contents));

		return appletConsent;
	}

	@Override
	public ProgressEvents parseProgressEvents(String contents) {
		final ProgressEvents progressEvents = new ProgressEvents();
		parseProgressEvents(progressEvents, getDocumentRoot(contents));

		return progressEvents;
	}

	@Override
	public UserContacts parseContacts(String contents) {
		final UserContacts userContacts = new UserContacts();
		parsetUserContacts(userContacts, getDocumentRoot(contents));

		return userContacts;
	}

	private void parseCloudSpongeResponse(final CloudSpongeResponse response,
			final Node root) {
		response.setImportId(evaluateXPath("import-id", root));
		response.setUserId(evaluateXPath("user-id[not(@nil)]", root));
		response.setEcho(evaluateXPath("echo[not(@nil)]", root));
	}

	private void parseImportResponse(final ImportResponse importResponse,
			final Node root) {
		parseCloudSpongeResponse(importResponse, root);

		importResponse.setStatus(evaluateXPath("status", root));
		importResponse.setErrorMessage(evaluateXPath("error/message", root));
	}

	private void parseUserConsent(final UserConsent userConsent, final Node root) {
		parseImportResponse(userConsent, root);

		userConsent.setUrl(evaluateXPath("url", root));
	}

	private void parseAppletConsent(final AppletConsent userConsent,
			final Node root) {
		parseImportResponse(userConsent, root);

		userConsent.setAppletUrl(evaluateXPath("url", root));
	}

	private void parseProgressEvents(final ProgressEvents progressEvents,
			final Node root) {
		parseCloudSpongeResponse(progressEvents, root);

		final NodeList events = (NodeList) evaluateXPath("//event", root,
				XPathConstants.NODESET);
		for (int i = 0;; i++) {
			Node eventNode = events.item(i);
			if (eventNode != null) {
				progressEvents.addEvent(parseEvent(eventNode));
			} else {
				break;
			}
		}
	}

	private Event parseEvent(Node eventNode) {
		return new Event(evaluateXPath("event-type", eventNode), evaluateXPath(
				"status", eventNode), evaluateXPath("value", eventNode));
	}

	private void parsetUserContacts(final UserContacts userContacts,
			final Node root) {
		final Node contactsOwner = (Node) evaluateXPath("contacts-owner", root,
				XPathConstants.NODE);
		userContacts.setOwner(parseContact(contactsOwner));

		final NodeList contacts = (NodeList) evaluateXPath("contacts/contact",
				root, XPathConstants.NODESET);
		for (int i = 0;; i++) {
			final Node contact = contacts.item(i);
			if (contact != null) {
				userContacts.addContact(parseContact(contact));
			} else {
				break;
			}
		}

		parseCloudSpongeResponse(userContacts, root);
	}

	private Contact parseContact(final Node contactNode) {
		// Removing from document for optimized XPath evaluation on large files
		contactNode.getParentNode().removeChild(contactNode);

		Contact contact = new Contact();
		contact.setFirstName(evaluateXPath("first-name", contactNode));
		contact.setLastName(evaluateXPath("last-name", contactNode));

		parseEmailAddresss(contactNode, contact);
		parsePhoneNumbers(contactNode, contact);

		return contact;
	}

	private void parseEmailAddresss(final Node contactNode, Contact contact) {
		final NodeList emailAddresses = (NodeList) evaluateXPath("email/email",
				contactNode, XPathConstants.NODESET);
		for (int i = 0;; i++) {
			Node emailNode = emailAddresses.item(i);
			if (emailNode != null) {
				contact.addEmail(parseEmail(emailNode));
			} else {
				break;
			}
		}
	}

	private Email parseEmail(Node emailNode) {
		final Email email = new Email();
		email.setEmailAddress(evaluateXPath("address", emailNode));
		email.setType(evaluateXPath("type", emailNode));

		return email;
	}

	private void parsePhoneNumbers(final Node contactNode, final Contact contact) {
		NodeList phoneNumbers = (NodeList) evaluateXPath("phone/phone",
				contactNode, XPathConstants.NODESET);
		for (int i = 0;; i++) {
			Node phoneNode = phoneNumbers.item(i);
			if (phoneNode != null) {
				contact.addPhoneNumber(parsePhone(phoneNode));
			} else {
				break;
			}
		}
	}

	private PhoneNumber parsePhone(Node phoneNode) {
		final PhoneNumber phoneNumber = new PhoneNumber();
		phoneNumber.setNumber(evaluateXPath("number", phoneNode));
		phoneNumber.setPhoneType(evaluateXPath("type", phoneNode));

		return phoneNumber;
	}

	private Node getDocumentRoot(final String contents) {
		try {
			final DocumentBuilder documentBuilder =
					documentBuilderFactory.newDocumentBuilder();
			final Document document = documentBuilder.parse(
					new InputSource(new StringReader(contents)));

			return document.getDocumentElement();
		} catch (ParserConfigurationException e) {
			throw new CloudSpongeParserException("Malformed Xml", e);
		} catch (SAXException e) {
			throw new CloudSpongeParserException("Malformed Xml", e);
		} catch (IOException e) {
			throw new CloudSpongeParserException("Malformed Xml", e);
		}
	}

	private String evaluateXPath(final String expression, final Node node) {
		final Node valueNode = (Node) evaluateXPath(expression, node,
				XPathConstants.NODE);
		if (valueNode != null) {
			final String value = (String) evaluateXPath(expression, node,
					XPathConstants.STRING);
			if (value != null) {
				return value.trim();
			}
			return value;
		}
		return null;
	}

	private Object evaluateXPath(final String expression, final Node node,
			final QName returnType) {
		try {
			return xPath.evaluate(expression, node, returnType);
		} catch (XPathExpressionException e) {
			throw new CloudSpongeParserException("Unexpected XML Layout.", e);
		}
	}

	static String readResource(final String resource, final String encoding) {
		final InputStream snippet =
				CloudSpongeParserImpl.class.getResourceAsStream(resource);
		try {
			Reader reader = new InputStreamReader(snippet, encoding);
			try {
				StringBuilder buffer = new StringBuilder();
				final char[] tmp = new char[1024];
				int l;
				while ((l = reader.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				return buffer.toString();
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
