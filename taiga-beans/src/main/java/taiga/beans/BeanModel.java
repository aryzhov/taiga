package taiga.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;
import taiga.model.AbstractModel;
import taiga.model.AbstractNode;
import taiga.model.AbstractProperty;
import taiga.model.ListNode;
import taiga.model.MapNode;

public class BeanModel extends AbstractModel implements Model {
	
	private Map<Class<?>, BeanClassDesc> cache = new HashMap<Class<?>, BeanClassDesc>();
	protected DefaultClassNameMap classNameMap = new DefaultClassNameMap(true);
	
	private BeanClassDesc getBeanDesc(Class<?> cl) throws ModelException {
		BeanClassDesc desc = cache.get(cl);
		if(desc == null) {
			try {
				BeanInfo bi = Introspector.getBeanInfo(cl);
				desc = new BeanClassDesc(this, bi);
				cache.put(cl, desc);
			} catch (IntrospectionException e) {
				throw new ModelException(e, ErrorCode.BEAN_INTROSPECTION_ERROR, cl.getName());
			}
		}
		return desc;
	}
	
	@Override
	public Property createNode(Node parent, Object typeName) throws ModelException {
		if(!(typeName instanceof Class<?>))
			throw new ModelException(ErrorCode.INVALID_TYPE_NAME_CLASS, typeName == null ? null : typeName.getClass());
		Class<?> nodeClass = (Class<?>)typeName;
		return createProperty(nodeClass);
	}

	@Override
	public AbstractProperty<?> createProperty(Class<?> cl, AbstractNode<?> node, Object propName) throws ModelException {
		return createProperty(cl);
	}
	
	@SuppressWarnings("unchecked")
	protected AbstractProperty<?> createProperty(Class<?> cl) throws ModelException {
		if(isPrimitiveType(cl)) {
			return new BeanProperty(this, cl);
		} else if(Collection.class.isAssignableFrom(cl)) {
			return createListNode((Class<Collection<Object>>)cl, Object.class);
		} else if (Map.class.isAssignableFrom(cl)) {
			return createMapNode((Class<Map<Object,Object>>)cl, Object.class);
		} else { 
			BeanClassDesc bcd = getBeanDesc(cl);
			return createBeanNode((Class<Object>)cl, bcd);
		}
	}

	protected <T> AbstractNode<T> createBeanNode(Class<T> cl, BeanClassDesc bcd) {
		return new BeanNode<T>(this, cl, bcd);
	}
	
	protected <V> AbstractNode<Collection<V>> createListNode(Class<Collection<V>> cl, Class<V> elemClass) {
		return new ListNode<V>(this, cl, elemClass);
	}
	
	private <V> AbstractNode<Map<Object, V>> createMapNode(Class<Map<Object,V>> cl, Class<V> elemClass) {
		return new MapNode<Object, V>(this, cl, Object.class, elemClass);
	}

//	@Override
//	public boolean isPrimitiveType(Class<?> cl) {
//		if(cache.containsKey(cl))
//			return false;
//		return super.isPrimitiveType(cl);
//	}
//
	public void addClassMapping(Class<?> cl, String publicName) {
		classNameMap.addClassMapping(cl, publicName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Node createAnonymousObjectNode() {
		return new MapNode(this, Map.class, Object.class, Object.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Array createAnonymousArrayNode() {
		return new ListNode(this, List.class, Object.class);
	}

	@Override
	public Property createNode(Class<?> expectedClass) throws ModelException {
		return createProperty(expectedClass);
	}

	@Override
	public Object parseTypeName(String name, Iterable<Object> scope) throws ModelException {
		Class<?> cl = classNameMap.getClassByName((String) name, scope);
		if(isPrimitiveType(cl)) {
			return cl;
		} else if(Collection.class.isAssignableFrom(cl)) {
			return cl;
		} else if (Map.class.isAssignableFrom(cl)) {
			return cl;
		} else { 
			BeanClassDesc bcd = getBeanDesc(cl);
			if(bcd != null)
				return cl;
		}
		return super.parseTypeName(name, scope);
	}
	
	@Override
	public boolean isAssignableFrom(Class<?> dest, Object src) {
		return dest.isAssignableFrom(src.getClass());
	}

	@Override
	public boolean isInstance(Class<?> cl, Object obj) {
		return cl.isInstance(obj);
	}
	
	@Override
	public Object convert(Object value, Class<?> expectedClass) throws ModelException {
		if(expectedClass == String.class)
			return value == null ? null : value.toString();
		else
			return super.convert(value, expectedClass);
	}
}
