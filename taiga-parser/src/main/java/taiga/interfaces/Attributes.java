package taiga.interfaces;

/** 
 * Attributes are like global variables -- they are visible in all templates except if there is a parameter with the same nameSrc that
 * hides the attribute.
 */
public interface Attributes {
	
	/**
	 *  Returns attribute value or throws a ModelException if the value is not defined.
	 * @throws ModelException if the attribute is not defined.
	 */
	public Object getAttr(String name) throws ModelException;
}
