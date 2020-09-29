package farm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HttpClient {

	public static void main(String[] args) {

		Options opts = Options.parse(args);
		try {
			execute(createURI(opts.uri()), opts.dest());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static URI createURI(String uriString) throws URISyntaxException {
		URI uri;
		uriString = Util.completionScheme(uriString);
		uri = new URI(uriString);
		if ("".equals(uri.getPath()))
			uri = new URI(uriString + "/");
		return uri;
	}

	private static HttpRequest createHttpRequest(URI uri) {
		HttpRequest request = new HttpRequest();
		request.setMethod("GET");
		request.setRequestURI(uri.getPath());
		request.setHttpVersion("HTTP/1.1");
		// TODO: fix set hostname correctly.
		request.setHeader("Host", "localhost");

		return request;
	}

	private static void execute(URI uri, String dest) {
		try {
			OutputStream os;
			if (dest.equals("-")) {
				os = System.out;
			} else {
				os = new FileOutputStream(new File(dest));
			}

			int port = uri.getPort();
			if (uri.getPort() == -1) {
				port = new Service(uri.getScheme()).getPort();
			}

			Socket socket = new Socket(uri.getHost(), port);

			HttpRequest req = createHttpRequest(uri);
			req.generate(socket.getOutputStream());

			HttpResponse response = HttpResponseParser.parse(socket.getInputStream(),
					false);

			response.writeBody(os);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (UnexpectedCharException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnknownServiceException e) {
			e.printStackTrace();
		}
	}
}
