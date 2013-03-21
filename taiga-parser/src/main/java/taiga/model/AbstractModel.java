package taiga.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;

public abstract class AbstractModel implements Model {

	@SuppressWarnings("unchecked")
	public final Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(
				Arrays.asList(Integer.class, Integer.TYPE, String.class, Boolean.TYPE, Boolean.class,
				Double.TYPE, Double.class, Float.TYPE, Float.class, Long.TYPE, Long.class));

	public boolean isPrimitiveType(Class<?> cl) {
		if(primitiveTypes.contains(cl))
			return true;
		return false;
	}
	
	public abstract AbstractProperty<?> createProperty(Class<?> cl, AbstractNode<?> node, Object propName) throws ModelException;

	@SuppressWarnings("unchecked")
	@Override
	public Array createAnonymousArrayNode() throws ModelException {
		return new ListNode(this, ArrayList.class, Object.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Node createAnonymousObjectNode() throws ModelException {
		return new MapNode(this, HashMap.class, Object.class, Object.class);
	}

	@Override
	public Object parseValue(String value, Class<?> expectedClass) throws ModelException {
		if(value == null || Object.class.equals(expectedClass) || String.class.equals(expectedClass))
			return value;
		throw new ModelException(ErrorCode.CANNOT_PARSE, value, expectedClass);
	}
	
	@Override
	public Object convert(Object value, Class<?> expectedClass) throws ModelException {
		throw new ModelException(ErrorCode.CANNOT_CONVERT, value, value == null ? null : value.getClass(), expectedClass);
	}
	
	@Override
	public Object parseTypeName(String name, Iterable<Object> scope) throws ModelException {
		return null;
	}
	
	@Override
	public Object parsePropertyName(Node parent, String propName) throws ModelException {
		return ((AbstractNode<?>)parent).parsePropertyName(propName);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Array createRange(int a, int b) throws ModelException {
		// TODO: optimize this by making a custom range node class
		ListNode result = new ListNode(this, ArrayList.class, Integer.class);
		result.setValue(new ArrayList(b < a ? 0 : b - a));
		for(int n = a; n <= b; n++)
			result.setPropertyValue(result.getNextPropName(), n);
		return result;
	}
}