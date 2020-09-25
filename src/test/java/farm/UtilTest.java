package farm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

public class UtilTest {
	@Test
	public void foo() throws URISyntaxException {
		URI uri = new URI("http://localhost:8080/index.html");
		assertEquals("localhost", uri.getHost());
	}

	@Test
	public void foo2() throws URISyntaxException {
		URI uri;
		String uriString = "localhost:8080/index.html";

		uri = new URI(uriString);
		assertNull(uri.getHost());

		String completion = Util.completionURI(uriString);
		uri = new URI(completion);
		assertEquals("http://localhost:8080/index.html", completion);
		assertEquals("localhost", uri.getHost());
	}

	@Test
	public void foo3() throws URISyntaxException {
		URI uri;
		String uriString = "localhost/index.html";

		uri = new URI(uriString);
		assertNull(uri.getHost());

		String completion = Util.completionURI(uriString);
		uri = new URI(completion);
		assertEquals("http://localhost/index.html", completion);
		assertEquals("localhost", uri.getHost());
	}
}
