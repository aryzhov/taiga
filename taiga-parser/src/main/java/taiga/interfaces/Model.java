package taiga.interfaces;

/** Model is a node factory for TaigaEngine */
public interface Model  {

	/**
	 * Creates a node of a native type.
	 */
	public Property createNode(Node parent, Object typeName) throws ModelException;

	public Property createNode(Class<?> expectedClass) throws ModelException;

	public Node createAnonymousObjectNode() throws ModelException;

	public Array createAnonymousArrayNode() throws ModelException;

	public Object parseTypeName(String name, Iterable<Object> scope) throws ModelException;
	
	public Object parsePropertyName(Node parent, String propName) throws ModelException;
	
	public Object parseValue(String value, Class<?> expectedClass) throws ModelException;

	public Object convert(Object value, Class<?> expectedClass) throws ModelException;
	
	public boolean isAssignableFrom(Class<?> dest, Object src);
	
	public boolean isInstance(Class<?> cl, Object obj);

	public Array createRange(int a, int b) throws ModelException;
}
