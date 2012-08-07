package com.cloudsponge.model;

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class AppletConsentTest {

	private String appletSnippet;

	private AppletConsent appletConsent;

	@Before
	public void setUp() throws Exception {
		appletSnippet = IOUtils.toString(
				getClass().getResourceAsStream(
						"/com/cloudsponge/applet.snippet"), "UTF-8");
		appletConsent = new AppletConsent(appletSnippet);
	}

	@Test
	public void generateAppletHtmlSnippet() {
		appletConsent.setAppletUrl("about:blank");
		appletConsent.setImportId("0101010101");

		assertEquals(getExpectedAppletSnippet(appletConsent),
				appletConsent.getAppletHtmlCode());
	}

	private String getExpectedAppletSnippet(AppletConsent appletConsent) {
		return appletSnippet.replace("<%URL%>", appletConsent.getAppletUrl())
				.replace("<%IMPORT_ID%>", appletConsent.getImportId());
	}
}
