package com.cloudsponge;

import java.io.Closeable;

import com.cloudsponge.model.ImportResponse;
import com.cloudsponge.model.ProgressEvents;
import com.cloudsponge.model.UserContacts;

/**
 * Wrapper for the CloudSponge API.
 * @author andrenpaes
 * @param <T> Consent object returned by the selected contacts service
 * @see ContactsService
 */
public interface CloudSpongeApi<T extends ImportResponse> extends Closeable {

	/**
	 * Sets the user name to be used on imports that require a user name and
	 * password.
	 * @param userName user name
	 * @return
	 */
	CloudSpongeApi<T> userName(String userName);

	String getUserName();

	/**
	 * Sets the user password to be used on imports that require a user name and
	 * password.
	 * @param userPassword user password
	 * @return
	 */
	CloudSpongeApi<T> userPassword(String userPassword);

	String getUserPassword();

	/**
	 * Sets the <strong>optional</strong> userId sent in the requests.
	 * @param userId Any unique identifier you use to identify a user
	 * @return
	 */
	CloudSpongeApi<T> userId(String userId);

	String getUserId();

	/**
	 * Sets the <strong>optional</strong> echo string sent in the requests.Any
	 * @param echo Any customer defined string data to be returned in the
	 *             response
	 * @return
	 */
	CloudSpongeApi<T> echo(String echo);

	String getEcho();

	/**
	 * Starts the import process.
	 * @return the consent object for the used {@link ContactsService}
	 * @throws CloudSpongeIOException if a communication error occurs
	 * @throws CloudSpongeParserException if a parsing error occurs
	 */
	T beginImport() throws CloudSpongeIOException, CloudSpongeParserException;

	/**
	 * Returns the current progress of the import process
	 * @return progress information
	 * @see ProgressEvents
	 * @throws CloudSpongeIOException if a communication error occurs
	 * @throws CloudSpongeParserException if a parsing error occurs
	 */
	ProgressEvents getImportProgress() throws CloudSpongeIOException,
			CloudSpongeParserException;

	/**
	 * Fetches the user contacts from CloudSponge
	 * @return information about each contact
	 * @see UserContacts
	 * @throws CloudSpongeIOException if a communication error occurs
	 * @throws CloudSpongeParserException if a parsing error occurs
	 */
	UserContacts fetchContacts() throws CloudSpongeIOException,
			CloudSpongeParserException;

	/**
	 * Dispose the connections used to retrieve the contacts information. Must
	 * be called after user for immediate resources release.
	 */
	@Override
	void close();
}
