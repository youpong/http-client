package farm;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseParser {

	private Unreadable in;
	private boolean debug;

	private HttpResponseParser(Reader in, boolean debug) {
		this.in = new Unreadable(in);
		this.debug = debug;
	}

	public static HttpResponse parse(Reader reader, boolean debug)
			throws UnexpectedCharException {
		return new HttpResponseParser(reader, debug).parse();
	}

	/**
	 * generic-message = start-line *(message-header CRLF) CRLF [ message-body ]
	 * start-line = Request-Line | Status-Line
	 * 
	 * @param in
	 * @return
	 * @throws UnexpectedCharException
	 */
	private HttpResponse parse() throws UnexpectedCharException {
		HttpResponse response = new HttpResponse();

		try {
			statusLine(response);
			messageHeader(response);
			//if (response.hasMessageBody())
			messageBody(response);
		} catch (IOException e) {
			System.err.print(e);
			System.exit(1);
		}
		if (debug)
			System.out.println("Debug: " + in.getCopy());

		return response;
	}

	private void messageBody(HttpResponse response) throws IOException {
		int c;
		int maxLen = Integer.parseInt(response.getHeader("Content-Length"));
		StringBuffer buf = new StringBuffer();

		for (int len = 0; len < maxLen; len++) {
			if ((c = in.read()) == -1)
				break;
			buf.append((char) c);
		}
		response.setBody(buf.toString());
	}

	/**
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void messageHeader(HttpResponse response)
			throws IOException, UnexpectedCharException {
		Map<String, String> map = new HashMap<String, String>();

		int c;
		while ((c = in.read()) != -1) {
			// CRLF - end of header
			if (c == '\r') {
				consum('\n');
				break;
			}
			in.unread(c);

			// key
			StringBuffer key = new StringBuffer();
			while ((c = in.read()) != -1) {
				if (c == ':')
					break;
				key.append((char) c);
			}

			// SP
			consum(' ');

			// value
			StringBuffer value = new StringBuffer();
			while ((c = in.read()) != -1) {
				if (c == '\r') {
					consum('\n');
					break;
				}
				value.append((char) c);
			}

			map.put(key.toString(), value.toString());
		}

		response.setHeader(map);
	}

	/**
	 * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
	 * 
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void statusLine(HttpResponse response)
			throws IOException, UnexpectedCharException {
		int c;
		StringBuffer sbuf;

		// HTTP-Versoin
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		response.setHttpVersion(sbuf.toString());

		// Status-Code
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		response.setStatusCode(sbuf.toString());

		// Reason-Phrase
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == '\r') {
				consum('\n');
				break;
			}
			sbuf.append((char) c);
		}
		response.setResonPhrase(sbuf.toString());
	}

	private void consum(int expected) throws IOException, UnexpectedCharException {
		int c = in.read();
		if (c != expected)
			throw new UnexpectedCharException(
					"expected (" + expected + ") actually (" + c + ")");
	}
}

class Unreadable extends Reader {
	Reader reader;
	StringBuffer sbuf = new StringBuffer();
	int buf;
	boolean loaded = false;

	Unreadable(Reader reader) {
		this.reader = reader;
	}

	public int read() throws IOException {
		if (loaded) {
			loaded = false;
			return buf;
		}
		int c = reader.read();
		sbuf.append((char) c);
		return c;
	}

	void unread(int c) {
		loaded = true;
		buf = c;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public String getCopy() {
		return sbuf.toString();
	}
}