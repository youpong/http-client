package farm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

public class UtilTest {
	@Test
	public void foo() {
		URI uri = URI.create("http://localhost:8080/topics/index.html");

		assertEquals("localhost", uri.getHost());
	}

	@Test
	public void testURI() {
		URI uri;

		uri = URI.create("http://localhost:8080");
		assertEquals("", uri.getPath());

		uri = URI.create("http://localhost:8080/");
		assertEquals("/", uri.getPath());

		uri = URI.create("http://localhost:8080/topics/");
		assertEquals("/topics/", uri.getPath());
	}

	@Test
	public void testFile() {
		File file;

		file = new File("");
		assertEquals("", file.getName());

		file = new File("/");
		assertEquals("", file.getName());

		file = new File("/topics");
		assertEquals("topics", file.getName());
		//assertEquals(false, file.isDirectory());

		file = new File("/topics/");
		assertEquals("topics", file.getName());
		//assertEquals(false, file.isDirectory());

		file = new File("/topics/index.html");
		assertEquals("index.html", file.getName());
	}

	@Test
	public void foo2() throws URISyntaxException {
		URI uri;
		String uriString = "localhost:8080/index.html";

		uri = new URI(uriString);
		assertNull(uri.getHost());

		String completion = Util.completionScheme(uriString);
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

		String completion = Util.completionScheme(uriString);
		uri = new URI(completion);
		assertEquals("http://localhost/index.html", completion);
		assertEquals("localhost", uri.getHost());
	}
}