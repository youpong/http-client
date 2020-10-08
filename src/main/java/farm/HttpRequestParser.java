package farm;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequestParser extends HttpMessageParser {

	private HttpRequestParser(InputStream is, boolean debug) {
		super(is, debug);
	}

	public static HttpRequest parse(InputStream is, boolean debug)
			throws UnexpectedCharException, IOException, NullRequestException {
		return new HttpRequestParser(is, debug).parse();
	}

	/**
	 * generic-message = start-line *(message-header CRLF) CRLF [ message-body ]
	 * start-line = Request-Line | Status-Line
	 * 
	 * @param in
	 * @return
	 * @throws UnexpectedCharException
	 * @throws NullRequestException
	 */
	private HttpRequest parse()
			throws IOException, UnexpectedCharException, NullRequestException {
		HttpRequest request = new HttpRequest();

		requestLine(request);
		messageHeader(request);
		if (request.hasMessageBody())
			messageBody(request);

		return request;
	}

	/**
	 * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
	 * 
	 * @param request
	 * @throws IOException
	 * @throws UnexpectedCharException
	 * @throws NullRequestException
	 */
	private void requestLine(HttpRequest request)
			throws IOException, UnexpectedCharException, NullRequestException {
		int c;
		StringBuffer sbuf;

		if ((c = is.read()) == -1) {
			throw new NullRequestException("");
		}
		is.unread(c);

		// Method
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setMethod(sbuf.toString());

		// Request-URI
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == ' ')
				break;
			sbuf.append((char) c);
		}
		request.setRequestURI(sbuf.toString());

		// HTTP-Versoin
		sbuf = new StringBuffer();
		while ((c = is.read()) != -1) {
			if (c == '\r') {
				consum('\n');
				break;
			}
			sbuf.append((char) c);
		}
		request.setHttpVersion(sbuf.toString());
	}

	private void messageBody(HttpRequest request) {
		// TODO Auto-generated method stub
	}
}
