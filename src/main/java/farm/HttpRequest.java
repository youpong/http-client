package farm;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private String method;
	private String requestURI;
	private String httpVersion;
	private Map<String, String> headerMap = new HashMap<String, String>();

	private String generateRequestLine() {
		return method + " " + requestURI + " " + httpVersion + "\r\n";
	}

	public String getMethod() {
		return method;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public boolean hasMessageBody() {
		// TODO
		return false;
	}

	public void setAllHeaders(Map<String, String> map) {
		headerMap.putAll(map);
	}

	public void setHeader(String key, String value) {
		headerMap.put(key, value);
	}

	public String getHeader(String key) {
		return headerMap.get(key);
	}

	public void generate(Writer writer) throws IOException {
		writer.write(generateRequestLine());
		for (var entry : headerMap.entrySet()) {
			writer.write(entry.getKey() + ": " + entry.getValue() + "\r\n");
		}
		writer.write("\r\n");
		writer.flush();
	}
}
