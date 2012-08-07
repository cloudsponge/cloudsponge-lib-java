package com.cloudsponge;

import com.cloudsponge.model.ImportResponse;

/**
 * Creates instances of the {@link CloudSpongeApi}.<br>
 * Should be injected (or created directly) in your application code. This makes
 * it easier for unit testing, since it's possible to replace the factory by a
 * mock object that creates mock {@link CloudSpongeApi} objects.<br>
 * Generally, there should be only one instance of this class for your
 * application.
 * 
 * @author andrenpaes
 * @see CloudSpongeApiFactoryImpl
 */
public interface CloudSpongeApiFactory {

	/**
	 * Creates an instance of the {@link CloudSpongeApi} for the specified
	 * {@link ContactsService}.
	 * @param contactsService contacts serivce
	 * @return created and ready to use instance
	 * @see 
	 */
	<T extends ImportResponse> CloudSpongeApi<T> create(ContactsService<T> contactsService);
}
