package farm;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
//import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

	public static void main(String[] args) {
		URI uri;

		if (args.length != 1) {
			System.out.println("http-client uri");
			System.exit(1);
		}
		try {
			uri = new URI(args[0]);
			execute(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static HttpRequest createHttpRequest(URI uri) {
		HttpRequest request = new HttpRequest();
		request.setMethod("GET");
		request.setRequestURI(uri.getPath());
		request.setHttpVersion("HTTP/1.1");
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Host", "localhost");
		request.setHeader(headers);

		return request;
	}

	private static void execute(URI uri) {
		try {
			Socket socket = new Socket(uri.getHost(), uri.getPort());
			Writer writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write(createHttpRequest(uri).generate());
			writer.flush();
			HttpResponse response = HttpResponseParser
					.parse(new InputStreamReader(socket.getInputStream()), false);

			System.out.print(response.getBody());
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
