package farm;

public class Util {

	public static String completionURI(String uri) {
		if (! uri.startsWith("http:")) {
			return "http://" + uri;
		}

		return uri;
	}
}
