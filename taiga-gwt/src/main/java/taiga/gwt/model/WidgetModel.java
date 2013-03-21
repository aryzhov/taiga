package taiga.gwt.model;

import java.util.HashMap;
import java.util.Map;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;
import taiga.model.AbstractNode;
import taiga.model.AbstractProperty;
import taiga.model.CoreModel;

public class WidgetModel extends CoreModel {

	private Map<String, ClassDesc<?>> typeDescs = new HashMap<String, ClassDesc<?>>();
	private Map<Class<?>, ClassDesc<?>> classDescs = new HashMap<Class<?>, ClassDesc<?>>();
	
	public WidgetModel() {
		HasTextDesc hasText = new HasTextDesc();
		HasHTMLDesc hasHTML = new HasHTMLDesc(hasText);
		add(new HTMLDesc(hasHTML));
		add(new PageDesc());
	}
	
	private void add(ClassDesc<?> wd) {
		if(wd.getTypeName() != null)
			typeDescs.put(wd.getTypeName(), wd);
		classDescs.put(wd.getWidgetClass(), wd);
	}
	
	@Override
	public Property createNode(Node parent, Object typeName) throws ModelException {
		if(typeName instanceof ClassDesc<?>)
			return ((ClassDesc<?>)typeName).createNode(this);
		return super.createNode(parent, typeName);
	}
	
	@Override
	public AbstractProperty<?> createProperty(Class<?> cl, AbstractNode<?> node, Object propName) throws ModelException {
		ClassDesc<?> wd = classDescs.get(cl);
		if(wd != null)
			return wd.createNode(this);
		else
			return super.createProperty(cl, node, propName);
	}
	
	@Override
	public Object parseTypeName(String name, Iterable<Object> scope) throws ModelException {
		return typeDescs.get(name);
	}
}
