package farm;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
	private Unreadable in;
	private boolean debug;

	private HttpRequestParser(Reader in, boolean debug) {
		this.in = new Unreadable(in);
		this.debug = debug;
	}

	public static HttpRequest parse(Reader reader, boolean debug)
			throws UnexpectedCharException {
		return new HttpRequestParser(reader, debug).parse();
	}

	/**
	 * generic-message = start-line *(message-header CRLF) CRLF [ message-body ]
	 * start-line = Request-Line | Status-Line
	 * 
	 * @param in
	 * @return
	 * @throws UnexpectedCharException
	 */
	private HttpRequest parse() throws UnexpectedCharException {
		HttpRequest request = new HttpRequest();

		try {
			requestLine(request);
			messageHeader(request);
			if (request.hasMessageBody())
				messageBody(request);
		} catch (IOException e) {
			System.err.print(e);
			System.exit(1);
		}
		if (debug)
			System.out.println("Debug: " + in.getCopy());

		return request;
	}

	private void messageBody(HttpRequest request) {
		// TODO Auto-generated method stub
	}

	/**
	 * @param request
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void messageHeader(HttpRequest request)
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

		request.setAllHeaders(map);
	}

	/**
	 * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
	 * 
	 * @param request
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void requestLine(HttpRequest request)
			throws IOException, UnexpectedCharException {
		int c;
		StringBuffer sbuf;

		// Method
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setMethod(sbuf.toString());

		// Request-URI
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setRequestURI(sbuf.toString());

		// HTTP-Versoin
		sbuf = new StringBuffer();
		while ((c = in.read()) != -1) {
			if (c == '\r') {
				consum('\n');
				break;
			}
			sbuf.append((char) c);
		}
		request.setHttpVersion(sbuf.toString());
	}

	private void consum(int expected) throws IOException, UnexpectedCharException {
		int c = in.read();
		if (c != expected)
			throw new UnexpectedCharException(
					"expected (" + expected + ") actually (" + c + ")");
	}
}
