package taiga.interfaces;

import java.util.Set;

/** 
 * Node is an object that can have properties.
 */
public interface Node extends Property {

	public Class<?> getPropertyNameClass();
		
	/** Returns a specified property */
	public Property getProperty(Object propName) throws ModelException;

	/** Returns all property names */
	public Set<?> getPropertyNames();
	
	/** Returns true if the specified property exists */
	public boolean hasProperty(Object propName);

	/** Returns true if the specified property has a value */
	public boolean isPropertyValueSet(Object propName);
	
	/** Returns the base class for the property */
	public Class<?> getPropertyBaseClass(Object propName) throws ModelException;
		
	/** 
	 * Sets property value. This has the same effect as getProperty(propName).setValue() but
	 * is potentially more efficient if the corresponding Property object hasn't been created yet.
	 * @throws ModelException if the property doesn't support the SET operation or
	 * an exception has occurred in the SET method.
	 * @throws ClassCastException if the value is incompatible with property type
	 */
	public void setPropertyValue(Object propName, Object obj) throws ModelException;

	public Model getModel();
	
}