package taiga.model;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.interfaces.Property;
import taiga.interfaces.StringUtils;

public abstract class AbstractProperty<V> implements Property {

	protected Object name;
	protected AbstractNode<?> parent;
	protected Class<?> baseClass;
	protected V value = null;
	protected boolean valueSet = false;
	protected AbstractModel model;
	
	public AbstractProperty(AbstractModel model, Class<?> baseClass) {
		this.model = model;
		this.baseClass = baseClass;
	}

	@Override
	public Object getName() {
		return name;
	}

	@Override
	public Node getParent() {
		return parent;
	}
	
	@Override
	public Class<?> getValueClass() {
		return baseClass;
	}
	
	protected void checkValueSet() throws ModelException {
		if(!valueSet)
			throw new ModelException(ErrorCode.VALUE_NOT_SET, name);
		else if(value == null)
			throw new ModelException(ErrorCode.VALUE_NOT_SET, name);
	}
	
	@Override
	public Object getValue() throws ModelException {
		checkValueSet();
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(Class<T> cl) throws ModelException {
		if(value != null && !model.isAssignableFrom(cl, value))
			throw new ModelException(ErrorCode.INCOMPATIBLE_TYPES, value == null ? null : value.getClass(), cl);
		return (T)value;
	}
	
//	public boolean isValid() {
//		if(parent == null)
//			return initialized;
//		try {
//			return parent.hasProperty(name) && initialized && parent.getPropertyValue(name) == value;
//		} catch (ModelException e) {
//			return false;
//		}
//	}
//	
	@Override
	public void init(Params params, Iterable<Node> scope) throws ModelException {
		setValue(createInitValue(params, scope));
	}

	protected abstract V createInitValue(Params params, Iterable<Node> scope) throws ModelException;

	@SuppressWarnings("unchecked")
	public void init(Node parent, Object propName, Object value, boolean valueSet) throws ModelException {
		if(valueSet && value != null && !model.isInstance(baseClass, value))
			throw new ModelException(ErrorCode.INCOMPATIBLE_TYPES, baseClass, value.getClass());
		AbstractNode<?> parentNode = (AbstractNode<?>) parent;
		this.value = (V) value;
		this.name = propName;
		this.parent = parentNode;
		this.valueSet = valueSet;
	}

	@Override
	public boolean assignTo(Node parent, Object propName) throws ModelException {
		parent.setPropertyValue(propName, value);
		if(this.parent != null)
			return false;
		if(!(parent instanceof AbstractNode))
			return false;
		AbstractNode<?> parentNode = (AbstractNode<?>) parent;
		if(parentNode.model != model)
			return false;
		this.name = propName;
		this.parent = parentNode;
		parentNode.onAssign(propName, this);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setValue(Object value) throws ModelException {
		Object v;
		if(value == null || model.isInstance(baseClass, value)) {
			v = value;
		} else {
			v = model.convert(value, baseClass);
		}
		if(parent != null)
			parent.setPropertyValue(name, value);
		this.value = (V)v;
		valueSet = true;

	}
	
	@Override
	public boolean isValueSet() {
		return valueSet && value != null;
	}
	
	@Override
	public String toString() {
		return (name != null ? name + ":" : "") + StringUtils.toString(value);
	}
}