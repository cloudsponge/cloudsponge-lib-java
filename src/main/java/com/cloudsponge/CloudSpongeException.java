package com.cloudsponge;

/**
 * Root class for the exceptions thrown by the API.
 * @author andrenpaes
 */
public class CloudSpongeException extends RuntimeException {
	private static final long serialVersionUID = 3757635104836545461L;

	public CloudSpongeException() {
	}

	public CloudSpongeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CloudSpongeException(String message) {
		super(message);
	}

	public CloudSpongeException(Throwable cause) {
		super(cause);
	}	
}
