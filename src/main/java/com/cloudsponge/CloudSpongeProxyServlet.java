package com.cloudsponge;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

public final class CloudSpongeProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 6272123010097307245L;

	public static final String CLOUDSPONGE_ENDPOINT_URL = "CLOUDSPONGE_ENDPOINT_URL";

	private static final String LOCATION_HEADER = "Location";

	private String cloudSpongeEndPointUrl = "https://api.cloudsponge.com/auth";

	public String getCloudSpongeEndPointUrl() {
		return cloudSpongeEndPointUrl;
	}

	public void setCloudSpongeEndPointUrl(String cloudSpongeEndPointUrl) {
		this.cloudSpongeEndPointUrl = cloudSpongeEndPointUrl;
	}

	/**
	 * Creates a new instance of {@link CloudSpongeHttpServiceImpl} and returns it.<br>
	 * @return {@link CloudSpongeHttpService} instance
	 */
	private CloudSpongeHttpService getCloudSpongeHttpService() {
		final CloudSpongeHttpServiceImpl httpService = new CloudSpongeHttpServiceImpl();
		httpService.followRedirects(false);

		return httpService;
	}

	private String generateEndpointAuthUrl(HttpServletRequest req) {
		return cloudSpongeEndPointUrl + "?" + req.getQueryString();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final CloudSpongeHttpService httpService = getCloudSpongeHttpService();
		final String cloudSpongeEndPointUrl = generateEndpointAuthUrl(req);

		final HttpRequestBase get = httpService.createGet(cloudSpongeEndPointUrl);
		proxyRequestToCloudSponge(get, httpService, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final CloudSpongeHttpService httpService = getCloudSpongeHttpService();
		final String cloudSpongeEndPointUrl = generateEndpointAuthUrl(req);

		final HttpRequestBase post = httpService.createPost(cloudSpongeEndPointUrl, createFormFields(req));
		proxyRequestToCloudSponge(post, httpService, resp);
	}

	private Map<String, String> createFormFields(HttpServletRequest req) {
		final Map<String, String> fields = new LinkedHashMap<String, String>();
		@SuppressWarnings("unchecked")
		final Set<Entry<String, String[]>> parameters = req.getParameterMap().entrySet();
		for(Entry<String, String[]> parameter : parameters) {
			final String name = parameter.getKey();
			if (!name.equals(req.getQueryString())) {
				final String[] value = parameter.getValue();
				if (!isEmpty(value)) {
					fields.put(name, value[0]);
				} else {
					fields.put(name, null);
				}
			}
		}
		return fields;
	}

	private boolean isEmpty(String[] array) {
		return array == null || array.length == 0;
	}

	private void proxyRequestToCloudSponge(HttpRequestBase request,
			CloudSpongeHttpService httpService, HttpServletResponse servletResponse) throws IOException {
		try {
			final HttpResponse httpResponse = httpService.executeRaw(request);
			final HttpEntity entity = httpResponse.getEntity();
			try {
				final int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
				if (responseStatusCode == HttpServletResponse.SC_MOVED_TEMPORARILY) {
					servletResponse.sendRedirect(httpResponse.getFirstHeader(LOCATION_HEADER).getValue());
				} else {
					servletResponse.setStatus(responseStatusCode);
					final PrintWriter responseWriter = servletResponse.getWriter();
					responseWriter.write(EntityUtils.toString(entity));
					responseWriter.flush();
				}
			} finally {
				EntityUtils.consume(entity);
			}
		} finally {
			httpService.close();
		}
	}
}
