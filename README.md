cloudsponge-lib-java
====================

Java Wrapper for the CloudSponge.com Rest API
---------------------------------------------

CloudSponge is the tool that you need to go viral. Create an account at http://www.cloudsponge.com and integrate with this library. In a few lines of code you'll have access to your users' contact lists.

How to Use it
-------------

### First add it to your pom.xml ###

	<dependency>
	  <groupId>com.cloudsponge</groupId>
	  <artifactId>cloudsponge</artifactId>
	  <version>1.0-RC2</version>
	</dependency>

Or, if you're not using Maven, [download the jar](https://github.com/andrenpaes/cloudsponge-lib-java/downloads) and add it your project.

### Then use it! ###

	CloudSpongeApiFactory factory = new CloudSpongeApiFactoryImpl("DOMAIN_KEY", "DOMAIN_PASSWORD");

	CloudSpongeApi<UserConsent> cloudSponge = factory.create(ContactsService.GMAIL);
	// For AOL use:
	// final CloudSpongeApi<ImportResponse> cloudSponge = factory.create(ContactsService.AOL);
	// cloudSponge.userName("me").userPassword("test").beginImport();

	try {
		UserConsent consent = cloudSponge.beginImport();
		// Open consent page
		System.out.println(consent.getUrl());

		// Waiting for consent...
		ProgressEvents progress = cloudSponge.getImportProgress();
		while (!progress.isDone()) {
			progress = cloudSponge.getImportProgress();
			Thread.sleep(1000);
		}

		Event completeEvent = progress.getEvent(EventType.COMPLETE);
		if (completeEvent.getStatus() == EventStatus.ERROR) {
			System.out.println("Error: " + completeEvent.getValue());
		} else {
			UserContacts userContacts = cloudSponge.fetchContacts();
			Contact owner = userContacts.getOwner();
			System.out.println(owner);
			for (Contact contact : userContacts.getContacts()) {
				System.out.println(contact);
			}
		}
	} finally {
		cloudSponge.close();
	}

CloudSponge Proxy URL
---------------------
The CloudSponge API Java Wrapper also includes an [implementation of a Proxy URL](https://github.com/andrenpaes/cloudsponge-lib-java/blob/master/src/main/java/com/cloudsponge/CloudSpongeProxyServlet.java)
for branding the user authentication process. For more the details, click [here](http://www.cloudsponge.com/developer/branding#proxy-url).

### For using the Proxy URL Servlet, just add it to your web.xml file ###

	<servlet>
		<servlet-name>CloudSponge URL Proxy</servlet-name>
		<servlet-class>com.cloudsponge.CloudSpongeProxyServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>CloudSponge URL Proxy</servlet-name>
		<url-pattern>URL_PATTERN</url-pattern>
	</servlet-mapping>
