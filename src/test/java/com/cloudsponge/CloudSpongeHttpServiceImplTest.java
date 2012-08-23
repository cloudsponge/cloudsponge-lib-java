package com.cloudsponge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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


public class CloudSpongeHttpServiceImplTest {

	private static final int HTTP_TEST_SERVER_PORT = 38940;

	private CloudSpongeHttpServiceImpl cloudSpongeHttpService;

	private Server httpRemoteServer;


	@Before
	public void setUp() {
		cloudSpongeHttpService = new CloudSpongeHttpServiceImpl();
	}

	private void addTestHandlerToServer(Handler handler) throws Exception {
		httpRemoteServer = new Server();
		httpRemoteServer.addConnector(createConnector(HTTP_TEST_SERVER_PORT));
		httpRemoteServer.addHandler(handler);

		httpRemoteServer.start();
	}

	private static Connector createConnector(int port) {
		final Connector connector = new SocketConnector();
		connector.setHost("localhost");
		connector.setPort(port);
		
		return connector;
	}

	@After
	public void shutdown() throws Exception {
		cloudSpongeHttpService.close();
		if (httpRemoteServer != null) {			
			httpRemoteServer.stop();
		}
	}

	private String createUri(String path) {
		return "http://localhost:" + HTTP_TEST_SERVER_PORT  + path;
	}

	@Test
	public void execute_post_with_form() throws Exception {
		final String path = "/myUri";
		final Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("method", "create");

		final HttpPost post = cloudSpongeHttpService.createPost(
				createUri(path), fields);
		final String expectedResponse = "Post it!";

		addTestHandlerToServer(new AbstractHandler() {
			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException {
				assertEquals("POST", request.getMethod());
				assertEquals(path, target);
				assertEquals("create", request.getParameter("method"));

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print(expectedResponse);
				((Request) request).setHandled(true);
			}
		});
		assertEquals(expectedResponse, cloudSpongeHttpService.execute(post));
	}

	@Test
	public void execute_get_with_parameters() throws Exception {
		final String path = "/myUri";
		final HttpGet get = cloudSpongeHttpService.createGet(createUri(path));
		cloudSpongeHttpService.addParameter(get, "who", "nobody");
		final String expectedResponse = "Got it!";

		addTestHandlerToServer(new AbstractHandler() {
			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException {
				assertEquals("GET", request.getMethod());
				assertEquals(path, target);
				assertEquals("nobody", request.getParameter("who"));

				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().print(expectedResponse);
				((Request) request).setHandled(true);
			}
		});
		assertEquals(expectedResponse, cloudSpongeHttpService.execute(get));
	}

	@Test
	public void execute_get_with_authentication() throws Exception {
		final String path = "/protected/myUri";
		final HttpGet get = cloudSpongeHttpService.createGet(createUri(path));
		cloudSpongeHttpService.addAuthenticationCredentials(get, "user", "pass");
		final String expectedResponse = "Authorized!";

		addTestHandlerToServer(new AbstractHandler() {
			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException {
				final String authorization = request.getHeader("Authorization");
				if (authorization == null) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setHeader("WWW-Authenticate", "Basic realm=\"test\"");
				} else {
					assertEquals("Basic dXNlcjpwYXNz", authorization);

					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().append(expectedResponse);
				}
				((Request) request).setHandled(true);
			}
		});
		assertEquals(expectedResponse, cloudSpongeHttpService.execute(get));
	}

	@Test(expected = CloudSpongeIOException.class)
	public void execute_get_with_notFound_error() throws Exception {
		final String path = "/protected/myUri";
		final HttpGet get = cloudSpongeHttpService.createGet(createUri(path));
		
		addTestHandlerToServer(new AbstractHandler() {
			@Override
			public void handle(String target, HttpServletRequest request,
					HttpServletResponse response, int dispatch) throws IOException {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				((Request) request).setHandled(true);
			}
		});
		cloudSpongeHttpService.execute(get);
	}
}
