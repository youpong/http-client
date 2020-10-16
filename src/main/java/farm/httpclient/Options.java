package farm.httpclient;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.ListIterator;

public class Options {

	private String dest = null;
	private String uri = null;
	private long times = 1;

	public static Options parse(String[] origArgs) throws OptionParseException {
		Options opts = new Options();

		ListIterator<String> args = Arrays.asList(origArgs).listIterator();
		while (args.hasNext()) {
			String arg = args.next();
			if (arg.equals("-n")) {
				if (!args.hasNext())
					throw new OptionParseException(
							"option requires an argument -- 'n'");
				try {
					opts.times = Integer.parseInt(args.next());
				} catch (NumberFormatException e) {
					throw new OptionParseException(
							"invalid number format: " + e.getMessage());
				}
			} else if (opts.uri == null) {
				opts.uri = arg;
			} else if (opts.dest == null) {
				opts.dest = arg;
			} else {
				throw new OptionParseException("too many arguments");
			}
		}

		if (opts.uri == null) {
			throw new OptionParseException("missing URL");
		}

		return opts;
	}

	public String uri() {
		return uri;
	}

	public String dest() {
		return dest;
	}

	public long times() {
		return times;
	}

	public static void printUsage(PrintStream out) {
		out.println(Client.PROG_NAME + " [-n <times>] uri [dest]");
	}
}
