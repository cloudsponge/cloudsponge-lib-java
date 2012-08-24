package com.cloudsponge;

import java.io.Closeable;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * Service for providing HTTP connections.<br>
 * Kind of a wrapper for Apache HttpComponents. Still uses HttpGet, HttpPost and
 * HttpRequestBase, but hide most of the other details.
 * 
 * @author andrenpaes
 */
interface CloudSpongeHttpService extends Closeable {

	HttpGet createGet(String uri);

	HttpPost createPost(String uri, Map<String, String> fields);

	void addAuthenticationCredentials(HttpRequestBase request, String user,
			String password);

	void addParameter(HttpRequestBase request, String paramName, String value);

	/**
	 * Executes the request, extracts the response and handle HTTP errors.
	 * @param request
	 * @return
	 * @throws CloudSpongeIOException
	 *             in case of IO Errors or in case of invalid HTTP Status Codes.
	 */
	String execute(HttpRequestBase request) throws CloudSpongeIOException;

	void followRedirects(boolean follow);

	/**
	 * Execute the request without extracting the response and without any HTTP
	 * Code error handling (it's all up to the client code).
	 * @param request request
	 * @return the response
	 * @throws CloudSpongeIOException in case of a IO Error
	 */
	HttpResponse executeRaw(HttpRequestBase request) throws CloudSpongeIOException;

	void close();
}
