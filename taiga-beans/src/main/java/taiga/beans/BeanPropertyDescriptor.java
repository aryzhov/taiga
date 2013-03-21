package taiga.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;


class BeanPropertyDescriptor {
	private Object propName;
	private Method readMethod;
	private Method writeMethod;
	private Class<?> type;
	private Class<?> objectType;
	
	public BeanPropertyDescriptor(Object propName, Method readMethod, Method writeMethod, Class<?> type, Class<?> objectType) {
		super();
		this.propName = propName;
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		this.type = type;
		this.objectType = objectType;
	}

	public Class<?> getBaseClass() {
		return objectType;
	}

	public Object get(BeanNode<?> node, Object bean) throws ModelException {
		try {
			Object result = readMethod.invoke(bean);
			return result;
		} catch (IllegalArgumentException e) {
			throw new ModelException(e, ErrorCode.PROPERTY_GET_FAILED, propName);
		} catch (IllegalAccessException e) {
			throw new ModelException(e, ErrorCode.PROPERTY_GET_FAILED, propName);
		} catch (InvocationTargetException e) {
			Exception ex = e;
			Throwable inner = e.getCause();
			if(inner instanceof Error) {
				if(!(inner instanceof AssertionError))
					throw (Error)inner;
			}
			else if(inner instanceof Exception)
				ex = (Exception) inner;
			throw new ModelException(ex, ErrorCode.PROPERTY_GET_FAILED, propName);
		}
	}

	public void set(BeanNode<?> node, Object bean, Object value) throws ModelException {
		Class<?> leftClass = node.getPropertyBaseClass(propName);
		if(writeMethod == null)
			throw new ModelException(ErrorCode.PROP_SET_NOT_SUPPORTED, propName);
		try {
			writeMethod.invoke(bean, value);
		} catch (IllegalArgumentException e) {
			throw new ModelException(e, ErrorCode.INCOMPATIBLE_TYPES, leftClass, value.getClass());
		} catch (IllegalAccessException e) {
			throw new ModelException(e, ErrorCode.PROPERTY_SET_FAILED, propName);
		} catch (InvocationTargetException e) {
			Exception ex = e;
			Throwable inner = e.getCause();
			if(inner instanceof Error) {
				if(!(inner instanceof AssertionError))
					throw (Error)inner;
			}
			else if(inner instanceof Exception)
				ex = (Exception) inner;
			throw new ModelException(ex, ErrorCode.PROPERTY_SET_FAILED, propName);
		}
	}

}
