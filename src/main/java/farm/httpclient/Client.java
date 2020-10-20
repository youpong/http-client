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
	public static final String HTTP_VERSION = "HTTP/1.1";
	public static final String PROG_NAME = "httpc";

	public static void main(String[] args) {
		try {
			Options opts = Options.parse(args);
			if (opts.times() == 1)
				execute(createURI(opts.uri()), opts.dest());
			else
				for (int i = 0; i < opts.times(); i++)
					execute(createURI(opts.uri()), opts.dest());
		} catch (OptionParseException e) {
			System.err.println(e.getMessage());
			Options.printUsage(System.err);
			System.exit(Http.EXIT_FAILURE);
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
		request.setHttpVersion(HTTP_VERSION);

		// headers

		if (uri.getPort() == -1)
			request.setHeader("Host", uri.getHost());
		else
			request.setHeader("Host", uri.getHost() + ":" + uri.getPort());

		request.setHeader("User-Agent", "Calf/0.1");

		return request;
	}

	private static void execute(URI uri, String dest) throws Exception {
		OutputStream os;
		if (dest == null) {
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
