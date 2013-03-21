package taiga.parser.lexer;


public class PushbackReader {
	private final String s;
	private int idx = 0;

	public PushbackReader(String s) {
		super();
		this.s = s;
	}

	public int read() {
		return idx == s.length() ? -1 : s.charAt(idx++);
	}
	
	public void unread(char c) {
		idx--;
		assert s.charAt(idx) == c; 
	}
}
