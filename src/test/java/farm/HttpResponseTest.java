package farm;

//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

public class HttpResponseTest {
	@Test
	public void foo() {
		HttpResponse response = new HttpResponse();
		response.setStatusCode("200");
		response.setHeader("Content-Type", "text/html");

		assertEquals("HTTP/1.1 200 OK\r\n", response.genStatusLine());
		assertEquals("Content-Type: text/html\r\n",
				response.genHeader("Content-Type"));
	}

	@Test
	public void testGen() throws IOException {
		HttpResponse response = new HttpResponse();
		response.setStatusCode("404");
		response.setHeader("Server", "bait/0.1");
		response.setHeader("Content-Type", "text/html");

		StringWriter w = new StringWriter();
		response.generate(w);

		//@formatter:off
		assertEquals("HTTP/1.1 404 Not Found\r\n" 
				+ "Server: bait/0.1\r\n"
				+ "Content-Type: text/html\r\n" 
				+ "Content-Length: 0\r\n" + "\r\n",
				w.toString());
		//@formatter:on
	}
}
