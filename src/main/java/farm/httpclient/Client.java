package farm.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import farm.Http;
import farm.HttpRequest;
import farm.HttpResponse;
import farm.HttpResponseParser;
import farm.Service;
import farm.Util;

public class Client {

	public static void main(String[] args) {
		Options opts = Options.parse(args);

		try {
			execute(createURI(opts.uri()), opts.dest());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(Http.EXIT_FAILURE);
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

		// request-line
		request.setMethod("GET");
		request.setRequestURI(uri.getPath());
		request.setHttpVersion("HTTP/1.1");

		// header
		if (uri.getPort() == -1)
			request.setHeader("Host", uri.getHost());
		else
			request.setHeader("Host", uri.getHost() + ":" + uri.getPort());
		request.setHeader("User-Agent", "Calf/0.1");

		return request;
	}

	private static void execute(URI uri, String dest) throws Exception {
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
	}
}
