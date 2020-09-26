package farm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class HttpClient {

	public static void main(String[] args) {
		URI uri;
		String dest = "-";

		if (args.length == 0 || args.length > 2) {
			System.out.println("http-client uri");
			System.exit(1);
		}
		if (args.length == 2) {
			dest = args[1];
		}

		try {
			uri = createURI(args[0]);
			execute(uri, dest);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
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
			Writer writer;
			if (dest.equals("-")) {
				writer = new OutputStreamWriter(System.out);
			} else {
				writer = new FileWriter(new File(dest));
			}

			Socket socket = new Socket(uri.getHost(), uri.getPort());

			HttpRequest req = createHttpRequest(uri);
			req.generate(new OutputStreamWriter(socket.getOutputStream()));

			HttpResponse response = HttpResponseParser
					.parse(new InputStreamReader(socket.getInputStream()), false);

			response.writeBody(writer);
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnexpectedCharException e) {
			// TODO
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
