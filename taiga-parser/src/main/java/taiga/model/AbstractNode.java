package taiga.model;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;

public abstract class AbstractNode<V> extends AbstractProperty<V> implements Node {

	public AbstractNode(AbstractModel model, Class<?> baseClass) {
		super(model, baseClass);
		this.model = model;
	}

	public abstract Class<?> getPropertyBaseClass(Object propName) throws ModelException;
	

	@Override
	public Property getProperty(Object propName) throws ModelException {
		boolean isSet = isPropertyValueSet(propName);
		Object value = isSet ? getPropertyValue(propName) : null;
		Class<?> baseClass = value != null ? value.getClass() : getPropertyBaseClass(propName);
		AbstractProperty<?> p = createProperty(propName, baseClass);
		p.init(this, propName, value, isSet);
		return p;
	}

	protected int getEstPropertyCount() {
		return 10;
	}

	protected AbstractProperty<?> createProperty(Object propName, Class<?> baseClass) throws ModelException {
		if(!hasProperty(propName))
			throw new ModelException(ErrorCode.PROPERTY_NOT_FOUND, propName);
		AbstractProperty<?> result = model.createProperty(baseClass, this, propName);
		return result;
	}

	public abstract Object getPropertyValue(Object propName) throws ModelException;
	
	@Override
	public abstract void setPropertyValue(Object propName, Object propValue) throws ModelException;

	/** This method is called by #AbstractProperty.assignTo */
	protected void onAssign(Object propName, AbstractProperty<?> prop) {
		assert prop.parent == this;
		assert propName.equals(prop.name);
	}

	@Override
	public Model getModel() {
		return model;
	}

	protected Object parsePropertyName(String propName) throws ModelException {
		return propName;
	}

	protected <T> T findInScope(Iterable<Node> scope, Class<T> cl) throws ModelException {
		for(Node n: scope) {
			if(cl == n.getValueClass())
				return n.getValue(cl);
		}
		throw new ModelException(ErrorCode.CANNOT_FIND_ENCLOSING_INSTANCE, cl);
	}

}
