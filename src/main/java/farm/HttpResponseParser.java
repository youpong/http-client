package farm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseParser {
	private PushbackInputStream is;
	private boolean debug;

	private HttpResponseParser(InputStream is, boolean debug) {
		this.is = new PushbackInputStream(is);
		this.debug = debug;
	}

	public static HttpResponse parse(InputStream is, boolean debug)
			throws UnexpectedCharException {
		return new HttpResponseParser(is, debug).parse();
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
		/*
		if (debug)
			System.out.println("Debug: " + in.getCopy());
		*/
		return response;
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
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		response.setHttpVersion(sbuf.toString());

		// Status-Code
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		response.setStatusCode(sbuf.toString());

		// Reason-Phrase
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == '\r') {
				consum('\n');
				break;
			}
			sbuf.append((char) c);
		}
		response.setResonPhrase(sbuf.toString());
	}

	/**
	 * @throws IOException
	 * @throws UnexpectedCharException
	 */
	private void messageHeader(HttpResponse response)
			throws IOException, UnexpectedCharException {
		Map<String, String> map = new HashMap<String, String>();

		int c;
		while ((c = is.read()) != -1) {
			// CRLF - end of header
			if (c == '\r') {
				consum('\n');
				break;
			}
			is.unread(c);

			// key
			StringBuffer key = new StringBuffer();
			while ((c = is.read()) != -1) {
				if (c == ':')
					break;
				key.append((char) c);
			}

			// SP
			consum(' ');

			// value
			StringBuffer value = new StringBuffer();
			while ((c = is.read()) != -1) {
				if (c == '\r') {
					consum('\n');
					break;
				}
				value.append((char) c);
			}

			map.put(key.toString(), value.toString());
		}

		response.setAllHeaders(map);
	}

	private void messageBody(HttpResponse response) throws IOException {
		int c;
		int maxLen = Integer.parseInt(response.getHeader("Content-Length"));
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		for (int len = 0; len < maxLen; len++) {
			if ((c = is.read()) == -1)
				break;
			os.write(c);
		}
		response.setBody(os.toByteArray());
	}

	private void consum(int expected) throws IOException, UnexpectedCharException {
		int c = is.read();
		if (c != expected)
			throw new UnexpectedCharException(
					"expected (" + expected + ") actually (" + c + ")");
	}
}
