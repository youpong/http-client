package farm;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	private final String HTTP_VERSION = "HTTP/1.1";
	private String contentType;
	private long contentLength;
	private String statusCode;
	private String serverName;
	private String httpVersion;
	private String reasonPhrase;
	private static Map<String, String> reasonPhraseMap;
	private Map<String, String> headerMap;
	private String body;

	static {
		reasonPhraseMap = new HashMap<String, String>();
		reasonPhraseMap.put("200", "OK");
		reasonPhraseMap.put("404", "Not Found");
		// reasonPhrase.put("405", "Method Not Allowed");
		reasonPhraseMap.put("501", "Not Implemented");
	}

	public String gen() {
		StringBuffer buf = new StringBuffer();
		buf.append(genStatusLine());
		buf.append(genServer());
		buf.append(genContentType());
		buf.append(genContentLength());
		buf.append("\r\n");
		return buf.toString();
	}

	public String genStatusLine() {
		String phrase = reasonPhraseMap.get(statusCode);

		return HTTP_VERSION + " " + statusCode + " " + phrase + "\r\n";
	}

	private String genServer() {
		return "Server: " + this.serverName + "\r\n";
	}

	public String genContentType() {
		return "Content-Type: " + contentType + "\r\n";
	}

	public String genContentLength() {
		return "Content-Length: " + contentLength + "\r\n";
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(long length) {
		this.contentLength = length;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setServer(String serverName) {
		this.serverName = serverName;
	}

	public String getBody() {
		return this.body;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setResonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setHeader(Map<String, String> map) {
		this.headerMap = map;
	}

	public String getHeader(String key) {
		if (headerMap == null)
			return null;
		return headerMap.get(key);
	}

	public void setBody(String body) {
		this.body = body;
	}
}
