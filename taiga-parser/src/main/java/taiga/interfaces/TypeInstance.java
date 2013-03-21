package taiga.interfaces;


public class TypeInstance {
	public final String typeName;
	
	public TypeInstance(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return typeName;
	}
}
