package farm;

import java.io.IOException;
import java.io.Reader;

class Unreadable extends Reader {
	Reader reader;
	StringBuffer sbuf = new StringBuffer();
	int buf;
	boolean loaded = false;

	Unreadable(Reader reader) {
		this.reader = reader;
	}

	public int read() throws IOException {
		if (loaded) {
			loaded = false;
			return buf;
		}
		int c = reader.read();
		sbuf.append((char) c);
		return c;
	}

	void unread(int c) {
		loaded = true;
		buf = c;
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public String getCopy() {
		return sbuf.toString();
	}
}