package farm;

public class Util {

	public static String completionURI(String uri) {
		if (uri.indexOf("http:") == -1) {
			return "http://" + uri;
		}

		return uri;
	}
}
