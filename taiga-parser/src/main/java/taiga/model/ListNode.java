package taiga.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.interfaces.Property;

public class ListNode<T> extends AbstractNode<Collection<T>> implements Array {

	private Class<T> elemClass;
	
	public ListNode(AbstractModel model, Class<? extends Collection<T>> baseClass, Class<T> elemClass) {
		super(model, baseClass);
		this.elemClass = elemClass;
	}

	@Override
	public Set<String> getPropertyNames() {
		Set<String> propNames = new HashSet<String>();
		for(int i = value.size()-1; i >= 0; i--)
			propNames.add(Integer.toString(i));
		return propNames;
	}

	@Override
	public boolean hasProperty(Object propName) {
		if(propName instanceof Integer) 
			return (Integer)propName >= 0 && (Integer)propName < value.size();
		else
			return false;
	}

	@Override
	public void setPropertyValue(Object propName, Object obj) throws ModelException {
		checkValueSet();
		if(!(propName instanceof Integer)) 
			throw new ModelException(ErrorCode.PROPERTY_NOT_FOUND, propName);
		int n = (Integer)propName;	
		if(obj != null && !model.isInstance(elemClass, obj))
			throw new ModelException(ErrorCode.INCOMPATIBLE_TYPES, elemClass, obj.getClass());
		@SuppressWarnings("unchecked")
		T elem = (T)obj;
		if(n < 0)
			return;
		for(int k = value.size(); k < n-1; k++)
			value.add(null);
		if(n < value.size())
			setElem(n, elem);
		else
			value.add(elem);
	}

	@Override
	public Object getPropertyValue(Object propName) throws ModelException {
		if(!(propName instanceof Integer)) 
			throw new ModelException(ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName);
		try {
			int n = (Integer)propName;	
			return getElem(n);
		} catch (IllegalArgumentException ex) {
			throw new ModelException(ex, ErrorCode.PROPERTY_NOT_FOUND, propName);
		}
	}

	@Override
	public Class<?> getPropertyBaseClass(Object propName) {
		return elemClass;
	}

	private Object getElem(int n) throws ModelException {
		if(value instanceof List) {
			try {
				return ((List<T>)value).get(n);
			} catch (IndexOutOfBoundsException ex) {
				throw new ModelException(ex, ErrorCode.INVALID_LIST_INDEX, n, value.size());
			}
		} else {
			int k = 0;
			for(T elem: value)
				if(k == n)
					return elem;
				else
					k++;
			throw new ModelException(ErrorCode.INVALID_LIST_INDEX, n, k);
		}
	}

	private void setElem(int n, T elem) throws ModelException {
		if(value instanceof List) {
			try {
				((List<T>)value).set(n, elem);
			} catch (UnsupportedOperationException ex) {
				throw new ModelException(ex, ErrorCode.LIST_SET_UNSUPPORTED);
			} catch (IndexOutOfBoundsException ex) {
				throw new ModelException(ex, ErrorCode.INVALID_LIST_INDEX, n, value.size());
			}
		} else {
			throw new ModelException(null, this, name, ErrorCode.LIST_SET_UNSUPPORTED);
		}
	}


	@Override
	public boolean isPropertyValueSet(Object propName) {
		if(propName instanceof String)
			try {
				int n = Integer.parseInt((String) propName);	
				return n > 0 && n < value.size();
			
			} catch (IllegalArgumentException ex) {
				return false;
			}
		else if(propName instanceof Integer) {
			int n = (Integer)propName;	
			return n >= 0 && n < value.size();
		} else
			return false;
	}

	@Override
	protected Collection<T> createInitValue(Params params, Iterable<Node> scope)
			throws ModelException {
		return new ArrayList<T>();
	}

	@Override
	public Class<?> getPropertyNameClass() {
		return Integer.class;
	}

	@Override
	public Object getNextPropName() throws ModelException {
		return value.size();
	}

	@Override
	public Object getLastPropName() throws ModelException {
		if(value.isEmpty())
			throw new ModelException(ErrorCode.ARRAY_IS_EMPTY);
		return value.size()-1;
	}
	
	@Override
	public Collection<Property> elements() throws ModelException {
		List<Property> result = new ArrayList<Property>(value.size());
		// TODO: optimize
		for(int i = 0; i < value.size(); i++)
			result.add(getProperty(i));
		return result;
	}


}
