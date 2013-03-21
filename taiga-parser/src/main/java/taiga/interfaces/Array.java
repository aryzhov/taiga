package taiga.interfaces;

import java.util.Collection;

public interface Array extends Node {
	
	public Object getNextPropName() throws ModelException;
	
	public Object getLastPropName() throws ModelException;
	
	public Collection<Property> elements() throws ModelException;
}
