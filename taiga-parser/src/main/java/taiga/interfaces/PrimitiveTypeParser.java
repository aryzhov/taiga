package taiga.interfaces;


public interface PrimitiveTypeParser {
	
	/** Returns true if the class nameSrc is a primitive type */
	public boolean isPrimitiveType(String className, Class<?> cl);
	
	/**
	 * Parses a value and returns a parsed value. If the value is null, returns NULL.
	 */
	public Object parseValue(String value, Class<?> cl) throws ModelException;
	
	
}
