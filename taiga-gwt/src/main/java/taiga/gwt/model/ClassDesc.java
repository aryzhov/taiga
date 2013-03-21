package taiga.gwt.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Params;

/** Wrapper around WidgetInfo */
public abstract class ClassDesc<W> {
	
	private final Set<Object> propNames = new HashSet<Object>();
	private final Map<Object, PropDesc<? super W,?>> propDesc = new HashMap<Object, PropDesc<? super W,?>>();
	
	public abstract String getTypeName();
	
	public abstract Class<?> getWidgetClass();
	
	public void add(PropDesc<? super W, ?> pd) {
		assert pd != null;
		Object propName = pd.getPropName();
		assert !propNames.contains(propName);
		propNames.add(propName);
		propDesc.put(propName, pd);
	}
	
	public void addInterface(ClassDesc<? super W> desc) {
		for(Object propName: desc.getPropertyNames())
			add(desc.getPropertyDescriptor(propName));
	}
	
	public Set<Object> getPropertyNames() {
		return propNames;
	}

	public PropDesc<? super W, ?> getPropertyDescriptor(Object name) {
		return propDesc.get(name);
	}

	public boolean hasProperty(Object name) {
		return propNames.contains(name);
	}
		
	public abstract W newInstance(Params params) throws ModelException;
	
	public WidgetNode<W> createNode(WidgetModel model) {
		return new WidgetNode<W>(model, getWidgetClass(), this);
	}

	protected static abstract class PropDesc<W, V> {
		
		public abstract String getPropName();
		
		public abstract Class<?> getBaseClass();
		
		public abstract V get(W widget) throws ModelException;
	
		public abstract void set(W widget, V value) throws ModelException;
	
	}
	
	protected static abstract class StringPropDesc<W> extends PropDesc<W, String> {
		@Override
		public Class<?> getBaseClass() {
			return Object.class;
		}
	}

	protected static abstract class BooleanPropDesc<W> extends PropDesc<W, Boolean> {
		@Override
		public Class<?> getBaseClass() {
			return Boolean.class;
		}
	}
	
	protected static abstract class IntegerPropDesc<W> extends PropDesc<W, Integer> {
		@Override
		public Class<?> getBaseClass() {
			return Integer.class;
		}
	}
	
	protected static abstract class IsWidgetPropDesc<W> extends PropDesc<W, IsWidget> {
		@Override
		public Class<?> getBaseClass() {
			return IsWidget.class;
		}
	}

	protected static abstract class WidgetPropDesc<W> extends PropDesc<W, Widget> {
		@Override
		public Class<?> getBaseClass() {
			return Widget.class;
		}
	}

}
