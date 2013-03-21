package taiga.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;

public class CoreModel extends AbstractModel {

	@Override
	public Property createNode(Node parent, Object typeName) throws ModelException {
		throw new ModelException(ErrorCode.TYPE_NOT_FOUND, typeName);
	}

	@Override
	public Property createNode(Class<?> cl) throws ModelException {
		if(isCollection(cl)) {
			return (AbstractProperty<?>) createAnonymousArrayNode();
		} else if (isMap(cl)) {
			return (AbstractProperty<?>) createAnonymousObjectNode();
		} else { 
			return new PrimitiveProperty(this, cl);
		}
	}

	protected boolean isMap(Class<?> expectedClass) {
		return Map.class.equals(expectedClass) || HashMap.class.equals(expectedClass);
	}

	protected boolean isCollection(Class<?> expectedClass) {
		return Collection.class.equals(expectedClass) || List.class.equals(expectedClass) || ArrayList.class.equals(expectedClass);
	}
	
	@Override
	public AbstractProperty<?> createProperty(Class<?> cl, AbstractNode<?> node, Object propName) throws ModelException {
		return (AbstractProperty<?>) createNode(cl);
	}

	@Override
	public boolean isAssignableFrom(Class<?> dest, Object src) {
		return true;
	}
	
	@Override
	public boolean isInstance(Class<?> cl, Object obj) {
		return true;
	}
}
