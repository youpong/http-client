package farm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
//import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpRequestTest {

	@Test
	public void generateAndParse() throws IOException, UnexpectedCharException {
		HttpRequest req = new HttpRequest();
		req.setMethod("GET");
		req.setRequestURI("/");
		req.setHttpVersion("HTTP/1.1");

		StringWriter w = new StringWriter();
		req.generate(w);
		req = null;
		HttpRequest req2 = HttpRequestParser.parse(new StringReader(w.toString()),
				false);

		assertEquals("GET", req2.getMethod());
		assertEquals("/", req2.getRequestURI());
		assertEquals("HTTP/1.1", req2.getHttpVersion());
	}

	@Test
	public void generateAndParse2() throws IOException, UnexpectedCharException {
		HttpRequest req = new HttpRequest();
		req.setMethod("POST");
		req.setRequestURI("/index.html");
		req.setHttpVersion("HTTP/1.0");
		req.setHeader("Host", "localhost");

		StringWriter w = new StringWriter();
		req.generate(w);
		req = null;
		HttpRequest req2 = HttpRequestParser.parse(new StringReader(w.toString()),
				false);

		assertEquals("POST", req2.getMethod());
		assertEquals("/index.html", req2.getRequestURI());
		assertEquals("HTTP/1.0", req2.getHttpVersion());
		assertEquals("localhost", req2.getHeader("Host"));
	}

}
