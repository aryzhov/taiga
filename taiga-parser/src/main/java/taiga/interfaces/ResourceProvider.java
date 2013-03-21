package taiga.interfaces;


/** 
 * An interface for reading a src file and other files referenced from it by absolute or relative names. 
 */
public interface ResourceProvider {

	public String getRelativeName();

	public String getAbsoluteName();

	/** Reads the resource 
	 * @throws ModelException if there was an I/O error */
	public String read() throws ModelException;
	
	/** Returns a resource provider for a relative or absolute path or null if not found */
	public ResourceProvider getResource(String name);
	
}
