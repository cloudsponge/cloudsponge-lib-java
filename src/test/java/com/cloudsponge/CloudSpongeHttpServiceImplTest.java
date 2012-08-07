package com.cloudsponge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

public class CloudSpongeHttpServiceImplTest {

	private CloudSpongeHttpServiceImpl cloudSpongeHttpService;

	private DefaultHttpClient httpClient;

	private Connection httpServer;

	@Before
	public void setUp() {
		httpClient = new DefaultHttpClient();
		cloudSpongeHttpService = new CloudSpongeHttpServiceImpl(httpClient);
	}

	@After
	public void shutdown() throws Exception {
		cloudSpongeHttpService.close();
		if (httpServer != null) {
			httpServer.close();
		}
	}

	private String createUri(String path) {
		return "http://localhost:38940" + path;
	}

	private void startServer(Container container) throws Exception {
		httpServer = new SocketConnection(container);
		SocketAddress address = new InetSocketAddress("localhost", 38940);
		httpServer.connect(address);
	}

	@Test
	public void execute_post_with_form() throws Exception {
		final String path = "/myUri";
		final Map<String, String> fields = new LinkedHashMap<String, String>();
		fields.put("method", "create");

		final HttpPost post = cloudSpongeHttpService.createPost(
				createUri(path), fields);
		final String expectedResponse = "Post it!";

		startServer(new Container() {
			@Override
			public void handle(Request req, Response resp) {
				try {
					assertEquals("POST", req.getMethod());
					assertEquals(path, req.getPath().getPath());
					assertEquals("create", req.getParameter("method"));

					PrintStream body = resp.getPrintStream();
					body.append(expectedResponse);
					resp.setCode(200);
					resp.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
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

		startServer(new Container() {
			@Override
			public void handle(Request req, Response resp) {
				try {
					assertEquals("GET", req.getMethod());
					assertEquals(path, req.getPath().getPath());
					assertEquals("nobody", req.getParameter("who"));

					PrintStream body = resp.getPrintStream();
					body.append(expectedResponse);
					resp.setCode(200);
					resp.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
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

		startServer(new Container() {
			@Override
			public void handle(Request req, Response resp) {
				try {
					final String authorization = req.getValue("Authorization");
					if (authorization == null) {
						resp.set("WWW-Authenticate", "Basic realm=\"test\"");
						resp.setCode(401);
					} else {
						assertEquals("Basic dXNlcjpwYXNz", authorization);

						final PrintStream body = resp.getPrintStream();
						body.append(expectedResponse);
						resp.setCode(200);
					}
					resp.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		assertEquals(expectedResponse, cloudSpongeHttpService.execute(get));
	}

	@Test(expected = CloudSpongeIOException.class)
	public void execute_get_with_notFound_error() throws Exception {
		final String path = "/protected/myUri";
		final HttpGet get = cloudSpongeHttpService.createGet(createUri(path));
		
		startServer(new Container() {
			@Override
			public void handle(Request req, Response resp) {
				try {
					resp.setCode(404);
					resp.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		cloudSpongeHttpService.execute(get);
	}
}
