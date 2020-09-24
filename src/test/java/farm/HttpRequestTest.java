package farm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
	public void bar() throws IOException, UnexpectedCharException {
		HttpRequest req = new HttpRequest();
		req.setMethod("GET");
		req.setRequestURI("/");
		req.setHttpVersion("HTTP/1.1");
		req.setHeader("Host", "localhost");

		StringWriter w = new StringWriter();
		req.generate(w);
		HttpRequest req2 = HttpRequestParser.parse(new StringReader(w.toString()),
				false);

		assertEquals("GET", req2.getMethod());
		assertEquals("/", req2.getRequestURI());
		assertEquals("HTTP/1.1", req2.getHttpVersion());
		assertEquals("localhost", req2.getHeader("Host"));
	}
}
