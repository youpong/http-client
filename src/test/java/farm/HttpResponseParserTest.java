package farm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

class HttpResponseParserTest {

	@Test
	void normal() throws UnexpectedCharException, IOException {
		// @formatter:off
		Reader reader = new StringReader(
				"HTTP/1.1 200 OK\r\n" +
				"Server: nginx/1.18.0 (Ubuntu)\r\n" +
				"Date: Tue, 15 Sep 2020 11:46:14 GMT\r\n" +
				"Content-Type: text/html\r\n" + 
				"Content-Length: 612\r\n" + 
				"Last-Modified: Sat, 22 Aug 2020 00:29:22 GMT\r\n" +
				"Connection: keep-alive\r\n" + 
				"ETag: \"5f4066e2-264\"\r\n" +
				"Accept-Ranges: bytes\r\n" + 
				"\r\n" + 
				"<!DOCTYPE html>\n" + 
				"<html>Hello</html>\n");
		// @formatter:on
		HttpResponse response = HttpResponseParser.parse(reader, false);

		assertEquals("HTTP/1.1", response.getHttpVersion());
		assertEquals("200", response.getStatusCode());
		assertEquals("OK", response.getReasonPhrase());

		assertEquals("612", response.getHeader("Content-Length"));
		assertEquals("keep-alive", response.getHeader("Connection"));

		StringWriter w = new StringWriter();
		response.writeBody(w);
		assertEquals("<!DOCTYPE html>\n" + "<html>Hello</html>\n", w.toString());
	}

	@Test
	void twoMsg() throws UnexpectedCharException, IOException {
		StringWriter w;
		// @formatter:off
		Reader reader = new StringReader(
				"HTTP/1.1 200 OK\r\n" +
				"Content-Length: 6\r\n" + 
				"\r\n" + 
				"body1\n" +
				"HTTP/1.1 200 OK\r\n" +
				"Content-Length: 7\r\n" + 
				"\r\n" + 
				"body 2\n");
		// @formatter:on
		HttpResponse response;
		response = HttpResponseParser.parse(reader, false);

		assertEquals("200", response.getStatusCode());
		w = new StringWriter();
		response.writeBody(w);
		assertEquals("body1\n", w.toString());

		response = HttpResponseParser.parse(reader, false);
		assertEquals("200", response.getStatusCode());
		w = new StringWriter();
		response.writeBody(w);
		assertEquals("body 2\n", w.toString());
	}
}
