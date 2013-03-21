package taiga.gwt.model;

import java.util.Set;

import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.model.AbstractNode;


public class WidgetNode<V> extends AbstractNode<V> {

	ClassDesc<V> desc;
	
	public WidgetNode(WidgetModel model, Class<?> baseClass, ClassDesc<V> desc) {
		super(model, baseClass);
		this.desc = desc;
	}

	@Override
	public Set<?> getPropertyNames() {
		return desc.getPropertyNames();
	}

	@Override
	public boolean hasProperty(Object propName) {
		return desc.hasProperty(propName);
	}

	@Override
	public void setPropertyValue(Object propName, Object obj) throws ModelException {
		ClassDesc.PropDesc pd1 = desc.getPropertyDescriptor(propName);
		pd1.set(value, obj);
	}
	
	@Override
	public Object getPropertyValue(Object propName) throws ModelException {
		ClassDesc.PropDesc pd = desc.getPropertyDescriptor(propName);
		if(pd == null)
			return null;
		return pd.get(value);
	}

	@Override
	public Class<?> getPropertyBaseClass(Object propName) {
		ClassDesc.PropDesc pd = desc.getPropertyDescriptor(propName);
		if(pd == null)
			return null;
		else
			return pd.getBaseClass();
	}
	
	@Override
	protected V createInitValue(Params params, Iterable<Node> scope) throws ModelException {
		return (V) desc.newInstance(params);
	}

	@Override
	public boolean isPropertyValueSet(Object propName) {
		ClassDesc.PropDesc pd = desc.getPropertyDescriptor(propName);
		if(pd == null)
			return false;
		return true;
	}

	@Override
	public Class<?> getPropertyNameClass() {
		return String.class;
	}
}
