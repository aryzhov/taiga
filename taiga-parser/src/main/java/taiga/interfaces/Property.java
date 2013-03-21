package taiga.interfaces;


/** 
 * Encapsulates a value which can be either standalone or can be a property of a node. 
 * A standalone value can become a property by calling assignTo(). Once assigned,
 * a a property cannot become unassinged or re-assigned to another node.
 * 
 * VALUE_NOT_SET --- init(Params) or setValue() ----> VALUE_SET ---- assignTo() -----> ASSIGNED
 */
public interface Property {
	
	/** Returns property nameSrc in parent or null if not assigned */
	public Object getName();
	
	/** Returns parent node or null if not assigned */
	public Node getParent();
	
	/** 
	 * Returns value class even if the value is null. Never returns null.
	 */
	public Class<?> getValueClass();

	/** Initializes the property with a  value. This method can be called only before #assignTo() */
	public void init(Params params, Iterable<Node> scope) throws ModelException;
	
	/**
	 * Assigns this Property object to a node.
	 * If the node belongs to the same model, this Property object becomes one of the children.
	 * If the node belogns to a different model, assigns the value using node.setPropertyValue() but
	 * doesn't assign the Property object. 
	 * 
	 * @returns true if the property object was assigned, and false if only the value was assigned.  
	 * @throws ModelException if the assignment cannot be completed
	 */
	public boolean assignTo(Node node, Object propName) throws ModelException;

	/** 
	 * Assigns value to the property. If the property is assigned to a node, changes sets property value to the node.
	 */
	public void setValue(Object value) throws ModelException;
	
	/** Returns true if the value has been set */
	public boolean isValueSet();
	
	/** 
	 * Returns the encapsulated value 
	 * @throws ModelException if the value is not set 
	 */
	public Object getValue() throws ModelException;

	/** Type-safe version of #getValue() */
	public <T> T getValue(Class<T> cl) throws ModelException;
	
}
