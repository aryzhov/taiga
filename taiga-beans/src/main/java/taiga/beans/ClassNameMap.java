package taiga.beans;

import taiga.interfaces.ModelException;

public interface ClassNameMap {
	public Class<?> getClassByName(String name, Iterable<Object> scope) throws ModelException;
}
