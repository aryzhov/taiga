package taiga.beans;

import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.model.AbstractNode;


public class BeanNode<V> extends AbstractNode<V> {

	BeanClassDesc desc;
	
	public BeanNode(BeanModel model, Class<V> baseClass, BeanClassDesc desc) {
		super(model, baseClass);
		this.desc = desc;
	}

	@Override
	public Set<?> getPropertyNames() {
		return desc.getPropertyNames();
	}

	@Override
	public boolean hasProperty(Object propName) {
		if(!(propName instanceof String))
			return false;
		else
			return desc.hasProperty((String)propName);
	}

	@Override
	public void setPropertyValue(Object propName, Object obj) throws ModelException {
		checkValueSet();
		BeanPropertyDescriptor pd1 = desc.getPropertyDescriptor(propName);
		pd1.set(this, value, obj);
	}
	
	@Override
	public Object getPropertyValue(Object propName) throws ModelException {
		BeanPropertyDescriptor pd = desc.getPropertyDescriptor(propName);
		if(pd == null)
			return null;
		return pd.get(this, value);
	}

	@Override
	public Class<?> getPropertyBaseClass(Object propName) throws ModelException {
		BeanPropertyDescriptor pd = desc.getPropertyDescriptor(propName);
		if(pd == null)
			throw new ModelException(null, this, propName, ErrorCode.PROPERTY_NOT_FOUND, propName);
		else
			return pd.getBaseClass();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected V createInitValue(Params params, Iterable<Node> scope) throws ModelException {
		return (V) desc.newInstance(params, scope);
	}

	@Override
	public boolean isPropertyValueSet(Object propName) {
		BeanPropertyDescriptor pd = desc.getPropertyDescriptor(propName);
		if(pd == null)
			return false;
		return true;
	}

	@Override
	public Class<?> getPropertyNameClass() {
		return String.class;
	}
	
	@Override
	protected int getEstPropertyCount() {
		return desc.getPropertyCount();
	}
}
