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
	  <version>1.0-RC1</version>
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
