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

	String execute(HttpRequestBase request) throws CloudSpongeIOException;

	HttpResponse executeRaw(HttpRequestBase request) throws CloudSpongeIOException;

	void close();
}
