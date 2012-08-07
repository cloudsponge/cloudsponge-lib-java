package com.cloudsponge;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

class CloudSpongeHttpServiceImpl implements CloudSpongeHttpService {
	private DefaultHttpClient httpClient;

	public CloudSpongeHttpServiceImpl(DefaultHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	public HttpGet createGet(String uri) {
		return new HttpGet(uri);
	}

	@Override
	public HttpPost createPost(String uri, Map<String, String> fields) {
		final Collection<NameValuePair> pairs = createPairs(fields);
		final HttpPost post = new HttpPost(uri);
		post.setEntity(new UrlEncodedFormEntity(pairs));

		return post;
	}

	private Collection<NameValuePair> createPairs(Map<String, String> fields) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : fields.entrySet()) {
			pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return pairs;
	}

	@Override
	public void addAuthenticationCredentials(HttpRequestBase request,
			String user, String password) {
		final URI getUri = request.getURI();
		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(getUri.getHost(), getUri.getPort()),
				new UsernamePasswordCredentials(user, password));
	}

	@Override
	public void addParameter(HttpRequestBase request, String paramName, String value) {
		if (StringUtils.isNotEmpty(value)) {
			try {
				request.setURI(new URIBuilder(request.getURI()).addParameter(paramName, value).build());
			} catch (URISyntaxException e) {/*Just ignore*/}
		}
	}

	@Override
	public String execute(HttpRequestBase request) throws CloudSpongeIOException {
		final String response;
		try {
			final HttpResponse httpResponse = httpClient.execute(request);
			verifyResponse(httpResponse);
			final HttpEntity entity = httpResponse.getEntity();
			response = EntityUtils.toString(entity);
		} catch (IOException e) {
			throw new CloudSpongeIOException(e);
		} finally {
			httpClient.getCredentialsProvider().clear();
			request.reset();
		}
		return response;
	}

	private void verifyResponse(HttpResponse httpResponse)
			throws CloudSpongeIOException {
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode >= 400) {
			throw new CloudSpongeIOException(statusCode);
		}
	}

	@Override
	public void close() {
		httpClient.getConnectionManager().shutdown();
	}
}
