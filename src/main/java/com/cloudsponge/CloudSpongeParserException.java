package com.cloudsponge;

/**
 * Thrown in case of an error during parsing in the import process.
 * 
 * @author andrenpaes
 */
public final class CloudSpongeParserException extends CloudSpongeException {
	private static final long serialVersionUID = -5288718711492860539L;

	public CloudSpongeParserException(String message, Throwable cause) {
		super(message, cause);
	}
}
