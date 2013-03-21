package taiga.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Params;
import taiga.interfaces.Property;
import taiga.model.AbstractModel;

public abstract class ContainerNode<N extends Node> extends TagNode<N> implements Array {

	public ContainerNode(AbstractModel model, Class<? extends N> baseClass) {
		super(model, baseClass);
	}

	@Override
	public Set<?> getPropertyNames() {
		Set<Attr> attrs = getAttributes();
		Set<Object> result = new HashSet<Object>();
		result.addAll(attrs);
		for(int i = value.getChildNodes().getLength()-1; i >= 0; i--)
			result.add(i);
		return result;
	}

	@Override
	public Object getPropertyValue(Object propName) throws ModelException {
		if(propName instanceof Attr)
			return super.getPropertyValue(propName);
		if(!(propName instanceof Integer))
			throw new ModelException(ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName);
		NodeList nl = value.getChildNodes();
		int n = (Integer) propName;
		if(n < 0 || n >= nl.getLength())
			throw new ModelException(ErrorCode.INVALID_LIST_INDEX, n, nl.getLength());
		return nl.item(n);
	}

	@Override
	public void setPropertyValue(Object propName, Object obj)
			throws ModelException {
		if(propName instanceof Attr) {
			super.setPropertyValue(propName, obj);
			return;
		}
		if(!(propName instanceof Integer))
			throw new ModelException(ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName);
		if(obj == null || !getChildNodeClass().isInstance(obj))
			throw new ModelException(ErrorCode.INCOMPATIBLE_TYPES, Node.class, obj.getClass());
		Node node = (Node)obj;
		int n = (Integer) propName;
		NodeList nl = value.getChildNodes();
		if(n != nl.getLength())
			throw new ModelException(ErrorCode.INVALID_LIST_INDEX, n, nl.getLength());
		value.appendChild(node);
	}
	
	@Override
	public boolean hasProperty(Object propName) {
		if(propName instanceof Attr) 
			return super.hasProperty(propName);
		if(!(propName instanceof Integer))
			return false;
		int n = (Integer) propName;
		return n >= 0 &&  n <= value.getChildNodes().getLength();
	}

	@Override
	public boolean isPropertyValueSet(Object propName) {
		return hasProperty(propName);
	}

	@Override
	public Object getNextPropName() throws ModelException {
		return value.getChildNodes().getLength();
	}

	@Override
	public Object getLastPropName() throws ModelException {
		int l = value.getChildNodes().getLength();
		if(l == 0)
			throw new ModelException(ErrorCode.ARRAY_IS_EMPTY);
		return l-1;
	}

	protected Class<? extends Node> getChildNodeClass() {
		return Node.class;
	}
	
	@Override
	public Class<?> getPropertyBaseClass(Object propName) throws ModelException {
		if(propName instanceof Integer)
			return getChildNodeClass();
		else
			return super.getPropertyBaseClass(propName);
	}

	@Override
	protected N createInitValue(Params params, Iterable<taiga.interfaces.Node> scope) throws ModelException {
		N result = super.createInitValue(params, scope);
//		if(result instanceof HTMLElement) {
//			HTMLElement el = (HTMLElement)result;
//			el.appendChild(getDocument().createTextNode(params.getParameter(0, String.class)));
//		}
		return result;
	}
	
	@Override
	public Collection<Property> elements() throws ModelException {
		List<Property> result = new ArrayList<Property>();
		for(int i = value.getChildNodes().getLength()-1; i >= 0; i--) {
			result.add(getProperty(i));
		}
		return result;
	}
}
