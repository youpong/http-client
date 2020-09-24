package farm;

import java.util.Map;

public class HttpRequest {
	private String method;
	private String requestURI;
	private String httpVersion;
	private Map<String, String> headerMap = null;

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

	public void setHeader(Map<String, String> map) {
		this.headerMap = map;
	}

	public String getHeader(String key) {
		if (headerMap == null)
			return null;
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
