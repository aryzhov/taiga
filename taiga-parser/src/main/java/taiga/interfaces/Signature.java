package taiga.interfaces;

public class Signature {
	private Class<?>[] signature;
	
	public Signature(Class<?>... signature) {
		this.signature = signature;
	}

	public boolean isEmpty() {
		return signature.length == 0;
	}

	public int size() {
		return signature.length;
	}

	public Class<?> get(int i) {
		return signature[i];
	}

	public static final Signature EMPTY = new Signature();
}
