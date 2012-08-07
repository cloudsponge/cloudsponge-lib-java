package com.cloudsponge;

public final class CloudSpongeIOException extends CloudSpongeException {
	private static final long serialVersionUID = -6763464235462398080L;

	private int responseCode;

	public CloudSpongeIOException(int responseCode) {
		super(getMessage(responseCode));
		this.responseCode = responseCode;
	}

	public CloudSpongeIOException(Throwable cause) {
		super(cause);
	}

	public int getResponseCode() {
		return responseCode;
	}

	private static String getMessage(int responseCode) {
		switch (responseCode) {
		case 400: // Bad Request
            return "Invalid parameters were supplied";
        case 401: // Unauthorized
            return "Access was denied to the requested resource. Either the supplied domain_key and domain_password did not match a domain or the request attempted to access a resource that you don’t have access to.";
        case 404: // Not Found
            return "The contacts are not yet available.";
        case 410: // Gone
            return "The contacts have already been retrieved and have been deleted from CloudSponge.com.";
        default:
            return "An unkown error occured.";
		}
	}
}
