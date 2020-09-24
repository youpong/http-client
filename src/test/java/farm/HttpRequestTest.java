package farm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpRequestTest {
	@Test
	public void foo() {
		HttpRequest req = new HttpRequest();
		req.setMethod("GET");
		req.setRequestURI("/");
		req.setHttpVersion("HTTP/1.1");
		//		req.setHeader("Host", "localhost");

		assertEquals("GET / HTTP/1.1\r\n", req.genRequestLine());
		/*		assertEquals("Host: localhost\r\n",
						req.genHeader("Content-Type"));
						*/
	}

	@Test
	public void bar() {
		HttpRequest req = new HttpRequest();
		req.setMethod("GET");
		req.setRequestURI("/");
		req.setHttpVersion("HTTP/1.1");
		req.setHeader("Host", "localhost");

		//String req.generate();
		StringReader r = new StringReader(req.generate());

	}

	//@Test
	public void testGen() {
		HttpResponse response = new HttpResponse();
		response.setStatusCode("404");
		response.setHeader("Server", "bait/0.1");
		response.setHeader("Content-Type", "text/html");

		//@formatter:off
		assertEquals("HTTP/1.1 404 Not Found\r\n" 
				+ "Server: bait/0.1\r\n"
				+ "Content-Type: text/html\r\n" 
				+ "Content-Length: 0\r\n" + "\r\n",
				response.gen());
		//@formatter:on
	}
}
