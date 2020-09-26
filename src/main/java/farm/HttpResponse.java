package farm;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	private final String HTTP_VERSION = "HTTP/1.1";
	private static Map<String, String> reasonPhraseMap;
	private String httpVersion;
	private String statusCode;
	private String reasonPhrase;
	private Map<String, String> headerMap = new HashMap<String, String>();
	private String body;

	static {
		reasonPhraseMap = new HashMap<String, String>();
		reasonPhraseMap.put("200", "OK");
		reasonPhraseMap.put("404", "Not Found");
		// reasonPhrase.put("405", "Method Not Allowed");
		reasonPhraseMap.put("501", "Not Implemented");
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public String getHttpVersion() {
		return httpVersion;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setResonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setHeader(String key, String value) {
		headerMap.put(key, value);
	}

	public void setAllHeaders(Map<String, String> map) {
		headerMap.putAll(map);
	}

	public String getHeader(String key) {
		return headerMap.get(key);
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void writeBody(Writer writer) throws IOException {
		writer.write(this.body);
		writer.flush();
	}

	//
	// Generate
	//

	public void generate(Writer writer) throws IOException {
		writer.write(generateStatusLine());
		writer.write(generateAllHeaders());
		writer.write("\r\n");
		//writer.write(genBody());
		writer.flush();
	}

	private String generateStatusLine() {
		return HTTP_VERSION + " " + statusCode + " " + reasonPhraseMap.get(statusCode)
				+ "\r\n";
	}

	private String generateAllHeaders() {
		StringBuffer buf = new StringBuffer();

		for (var entry : headerMap.entrySet()) {
			buf.append(generateHeader(entry.getKey()));
		}

		if (!headerMap.containsKey("Content-Length"))
			buf.append(generateContentLength());

		return buf.toString();
	}

	private String generateHeader(String key) {
		return key + ": " + getHeader(key) + "\r\n";
	}

	private String generateContentLength() {
		String len = getHeader("Content-Length");
		return "Content-Length: " + ((len == null) ? "0" : len) + "\r\n";
	}

}
