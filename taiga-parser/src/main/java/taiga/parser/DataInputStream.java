package taiga.parser;


public abstract class DataInputStream {
	private byte[] data;
	private int n;
	
	protected DataInputStream(int len, byte[] data) {
		this.data = new byte[len];
		int k = 0;
		for(int i = 0; i < data.length; i+= 2) {
			int m = data[i];
			byte b = data[i+1];
			k += m;
			this.data[k++] = b;
		}
		n = 0;
	}
	
	public int read() {
		if(n >= data.length)
			return -1;
		else
			return data[n++] & 0xFF;
	}
	
	public char readChar() {
        int ch1 = read();	
        int ch2 = read();
        return (char)((ch1 << 8) + (ch2 << 0));
	}
	
    public final int readInt() {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public void close() {
    }
}
