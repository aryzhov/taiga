package taiga.parser;

import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;


public interface ValueHolder {

	public static final Object ERROR = new Object(); 
	
	/** Returns null if there was an error */
	public Property getProperty(Class<?> expectedClass) throws TaigaException;
	
	/** Returns ERROR if there was an error while instantiating the value. */
	public Object getValue(Class<?> expectedClass) throws TaigaException;
	
}
