package taiga.beans;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.interfaces.Signature;
import taiga.interfaces.StringUtils;

/** Wrapper around BeanInfo */
public class BeanClassDesc {

	private final Model model;
	private final Set<String> propertyNames;
	private final String defaultProperty;
	private final Map<String, BeanPropertyDescriptor> propDesc;
	private Class<?> cl;
	private ConstructorSignature[] constructors;
	
	public BeanClassDesc(Model model, BeanInfo bi) {
		this.model = model;
		this.cl = bi.getBeanDescriptor().getBeanClass();
		Set<String> set = new HashSet<String>();
		Map<String, BeanPropertyDescriptor> map = new HashMap<String, BeanPropertyDescriptor>();
		for(PropertyDescriptor pd: bi.getPropertyDescriptors()) {
			if(StringUtils.equals("class", pd.getName(), true))
				continue;
			Method readMethod = pd.getReadMethod();
			Method writeMethod = pd.getWriteMethod();
			if(readMethod == null)
				continue;
			Class<?> type = readMethod.getReturnType();
			Class<?> objectType = getObjectType(type); 
			BeanPropertyDescriptor pd1 = new BeanPropertyDescriptor(pd.getName(), readMethod, writeMethod, type, objectType); 
			set.add(pd.getName());
			map.put(pd.getName(), pd1);
		}
		int dp = bi.getDefaultPropertyIndex();
		defaultProperty = dp >= 0 ? bi.getPropertyDescriptors()[dp].getName() : null;
		propertyNames = Collections.unmodifiableSet(set);		
		propDesc = Collections.unmodifiableMap(map);

		ArrayList<ConstructorSignature> clist = new ArrayList<ConstructorSignature>();
		Class<?> enclosingClass = cl.getEnclosingClass();
		for(Constructor c: cl.getConstructors()) {
			Class[] params = c.getParameterTypes();
			if(enclosingClass != null) {
				Class[] params1 = new Class[params.length-1];
				for(int i = 0; i < params1.length; i++)
					params1[i] = params[i+1];
				params = params1;
			}
			clist.add(new ConstructorSignature(c, params));
		}
		constructors = clist.toArray(new ConstructorSignature[clist.size()]);
	}

	
	
	private Class<?> getObjectType(Class<?> cl) {
		if(cl == Integer.TYPE)
			return Integer.class;
		else if(cl == Boolean.TYPE)
			return Boolean.class;
		else
			return cl;
	}

	
	public Model getModel() {
		return model;
	}

	public Set<String> getPropertyNames() {
		return propertyNames;
	}

	public String getDefaultProperty() {
		return defaultProperty;
	}

	public BeanPropertyDescriptor getPropertyDescriptor(Object name) {
		return propDesc.get(name);
	}

	public boolean hasProperty(String name) {
		return propertyNames.contains(name);
	}
		
	public Object newInstance(Params params, Iterable<Node> scope) throws ModelException {
		try {
			ConstructorSignature cs = params.selectSignature(constructors);
			Object[] paramValues = params.getParameterValues(cs);
			Class enclosingClass = cl.getEnclosingClass();
			if(enclosingClass != null) {
				Object enclosingInstance = getEnclosingInstance(scope, enclosingClass);
				Object[] paramValues1 = new Object[paramValues.length+1];
				paramValues1[0] = enclosingInstance;
				for(int i = 0; i < paramValues.length; i++)
					paramValues1[i+1] = paramValues[i];
				paramValues = paramValues1;
			}
			Object bean = cs.constructor.newInstance(paramValues);
			return bean;
		} catch (InstantiationException e) {
			throw new ModelException(e, ErrorCode.NEW_INSTANCE_ERROR, cl);
		} catch (IllegalAccessException e) {
			throw new ModelException(e, ErrorCode.NEW_INSTANCE_ERROR, cl);
		} catch (IllegalArgumentException e) {
			throw new ModelException(e, ErrorCode.NEW_INSTANCE_ERROR, cl);
		} catch (InvocationTargetException e) {
			throw new ModelException(e, ErrorCode.NEW_INSTANCE_ERROR, cl);
		}
	}

	private Object getEnclosingInstance(Iterable<Node> scope, Class enclosingClass) throws ModelException {
		for(Node n: scope) {
			if(n instanceof BeanNode) {
				Object o = n.getValue(); 
				if(o != null && enclosingClass.isInstance(o))
					return o;
			}
		}
		throw new ModelException(ErrorCode.CANNOT_INSTANTIATE_INNER, cl);
	}



	public int getPropertyCount() {
		return propertyNames.size();
	}

	private static class ConstructorSignature extends Signature {
		public Constructor constructor;

		private ConstructorSignature(Constructor constructor, Class[] params) {
			super(params);
			this.constructor = constructor;
		}
		
	}

}
