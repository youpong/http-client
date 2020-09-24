package farm;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	private String method;
	private String requestURI;
	private String httpVersion;
	private Map<String, String> headerMap = new HashMap<String, String>();

	public String genRequestLine() {
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

	public String generate() {
		StringBuffer buf = new StringBuffer();

		buf.append(genRequestLine());
		for (var entry : headerMap.entrySet()) {
			buf.append(entry.getKey() + ": " + entry.getValue() + "\r\n");
		}
		buf.append("\r\n");
		return buf.toString();
	}
}
