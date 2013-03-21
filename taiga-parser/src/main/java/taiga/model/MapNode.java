package taiga.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;

public class MapNode<K, T> extends AbstractNode<Map<K, T>> {

	private Class<K> keyClass;
	private Class<T> elemClass;

	public MapNode(AbstractModel model, Class<Map<Object, T>> cl, Class<K> keyClass, Class<T> elemClass) {
		super(model, cl);
		this.keyClass = keyClass;
		this.elemClass = elemClass; 
	}

	@Override
	public Set<?> getPropertyNames() {
		return value.keySet();
	}

	@Override
	public boolean hasProperty(Object name) {
		return value.containsKey(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setPropertyValue(Object propName, Object obj) throws ModelException {
		checkValueSet();
		if(obj != null && !model.isInstance(elemClass, obj))
			throw new ModelException(ErrorCode.INCOMPATIBLE_TYPES, elemClass, obj.getClass());
		if(propName != null && !model.isInstance(keyClass, propName))
			throw new ModelException(ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName != null ? propName.getClass() : null , keyClass);
		value.put((K)propName, (T) obj);
	}
	
	@Override
	public Object getPropertyValue(Object propName) throws ModelException {
		if(!value.containsKey(propName))
			throw new ModelException(ErrorCode.VALUE_NOT_SET, propName);
		return value.get(propName);
	}
	
	@Override
	public Class<?> getPropertyBaseClass(Object propName) {
		return elemClass;
	}

	@Override
	protected Map<K, T> createInitValue(Params params, Iterable<Node> scope)
			throws ModelException {
		return new HashMap<K, T>();
	}

	@Override
	public boolean isPropertyValueSet(Object propName) {
		return value != null && value.containsKey(propName);
	}

	@Override
	public Class<?> getPropertyNameClass() {
		return keyClass;
	}
}
