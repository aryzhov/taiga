package taiga.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;

public class DefaultClassNameMap implements ClassNameMap {

	private Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();
	private boolean allowJavaClassNames;
	private String defaultType;

	public DefaultClassNameMap(boolean allowJavaClassNames) {
		this.allowJavaClassNames = allowJavaClassNames;
		addClassMapping(List.class, "List");
		addClassMapping(Map.class, defaultType = "Map");
		addClassMapping(String.class, "String");
		addClassMapping(Integer.class, "Integer");
		addClassMapping(Date.class, "Date");
	}

	public void addClassMapping(Class<?> cl, String publicName) {
		classMap.put(publicName, cl);
	}
	
	@Override
	public Class<?> getClassByName(String className, Iterable<Object> scope) throws ModelException {
		Class<?> cl = classMap.get(className);
		if(cl != null) 
			return cl;
		if(allowJavaClassNames)
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException ex) {
				for(Object classObj: scope) {
					Class<?> cl1 = (Class<?>) classObj;
					String innerClassName = cl1.getName() + "$" + className.replaceAll("\\.", "\\$");
					try {
						return Class.forName(innerClassName);
					} catch (ClassNotFoundException ex1) {
						// ignore
					}
				}
				throw new ModelException(ex, ErrorCode.CLASS_NOT_FOUND, className);
			}
		throw new ModelException(ErrorCode.TYPE_NOT_FOUND, className);
	}

	public String getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(String defaultType) {
		this.defaultType = defaultType;
	}
	
}
