package com.cloudsponge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class CloudSpongeProxyServletTest {

	private static final int HTTP_PROXY_SERVLET_SERVER_PORT = 38940;

	private static final int HTTP_CLOUDSPONGE_SERVER_PORT = 38941;

	private Server proxyServletServer;

	private Server cloudSpongeServer;
	
	private CloudSpongeHttpService httpService;

	@Before
	public void setUp() throws Exception {
		httpService = new CloudSpongeHttpServiceImpl();

		proxyServletServer = new Server();
		proxyServletServer.addConnector(createConnector(HTTP_PROXY_SERVLET_SERVER_PORT));

		final Context test = new Context(proxyServletServer,"/test",Context.NO_SESSIONS);
		final CloudSpongeProxyServlet cloudSpongeProxyServlet = new CloudSpongeProxyServlet();
		cloudSpongeProxyServlet.setCloudSpongeEndPointUrl("http://localhost:" + HTTP_CLOUDSPONGE_SERVER_PORT + "/auth");
		test.addServlet(new ServletHolder(cloudSpongeProxyServlet), "/proxy");
		proxyServletServer.start();
	}

	private void startCloudSpongeMockServer(Handler testHandler) throws Exception {
		cloudSpongeServer = new Server();
		cloudSpongeServer.addConnector(createConnector(HTTP_CLOUDSPONGE_SERVER_PORT));
		cloudSpongeServer.addHandler(testHandler);
		cloudSpongeServer.start();
	}

	private Connector createConnector(int port) {
		final Connector connector = new SocketConnector();
		connector.setHost("localhost");
		connector.setPort(port);

		return connector;
	}

	@After
	public void shutDownServer() throws Exception {
		if (proxyServletServer != null) {
			proxyServletServer.stop();
		}
		if (cloudSpongeServer != null) {
			cloudSpongeServer.stop();
		}

		httpService.close();
	}

	private String createCloudSpongeProxyTestUrl(String queryString) {
		return "http://localhost:" + HTTP_PROXY_SERVLET_SERVER_PORT + "/test/proxy?" + queryString;
	}

	@Test
	public void proxy_post_method_to_cloudsponge() throws Exception {
		final Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("user", "test");
		fields.put("password", "test123");
		fields.put("empty", null);
		final HttpPost post = httpService.createPost(
				createCloudSpongeProxyTestUrl("123456"), fields);

		startCloudSpongeMockServer(new AbstractHandler() {
			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException {
				assertEquals("POST", request.getMethod());
				assertEquals("123456", request.getQueryString());
				assertEquals("test", request.getParameter("user"));
				assertEquals("test123", request.getParameter("password"));
				assertEquals("", request.getParameter("empty"));

				response.sendRedirect("about:blank");
				((Request) request).setHandled(true);
			}
		});

		final HttpResponse response = httpService.executeRaw(post);
		assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY,
				response.getStatusLine().getStatusCode());
		assertEquals("about:blank", response.getFirstHeader("Location").getValue());
	}

	@Test
	public void proxy_get_method_to_cloudsponge() throws Exception {
		final HttpGet get = httpService.createGet(createCloudSpongeProxyTestUrl("12345678"));

		startCloudSpongeMockServer(new AbstractHandler() {
			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException {
				assertEquals("GET", request.getMethod());

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().write("GOT IT!");
				((Request) request).setHandled(true);
			}
		});

		assertEquals("GOT IT!", httpService.execute(get));
	}
}
