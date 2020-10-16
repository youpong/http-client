package farm.httpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class OptionsTest {
	@Test
	public void arg1() throws Exception {
		String[] args = {"google.com"};
		Options opts = Options.parse(args);
		assertEquals("google.com", opts.uri());
		assertEquals(null, opts.dest());
		assertEquals(1, opts.times());
	}

	@Test
	public void args2() throws Exception {
		String[] args = {"google.com", "index.html"};
		Options opts = Options.parse(args);
		assertEquals("google.com", opts.uri());
		assertEquals("index.html", opts.dest());
	}

	@Test
	public void optionRequiresArg() {
		String[] args = {"-n"};
		OptionParseException e = assertThrows(OptionParseException.class,
				() -> Options.parse(args));

		assertEquals("option requires an argument -- 'n'", e.getMessage());
	}

	@Test
	public void optionRequireNumber() {
		String[] args = {"-n", "two"};

		OptionParseException e = assertThrows(OptionParseException.class,
				() -> Options.parse(args));

		assertEquals("invalid number format: For input string: \"two\"",
				e.getMessage());
	}

	@Test
	public void missingURL() {
		String[] args = {};

		OptionParseException e = assertThrows(OptionParseException.class,
				() -> Options.parse(args));

		assertEquals("missing URL", e.getMessage());
	}

	@Test
	public void tooManyArgs() {
		String[] args = {"one", "two", "three"};

		OptionParseException e = assertThrows(OptionParseException.class,
				() -> Options.parse(args));

		assertEquals("too many arguments", e.getMessage());
	}
}
