package taiga.html;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLElement;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.model.AbstractModel;
import taiga.model.AbstractNode;

public abstract class ElementNode<V extends Node> extends AbstractNode<V> {

	protected static final Set<Attr> ELEM_ATTRS = set(Attr.id, Attr.cl, Attr.title, Attr.style, Attr.lang);
	
	public ElementNode(AbstractModel model, Class<? extends V> baseClass) {
		super(model, baseClass);
	}

	@Override
	public Set<?> getPropertyNames() {
		return getAttributes();
	}
	
	protected abstract Set<Attr> getAttributes();

	@Override
	public boolean hasProperty(Object propName) {
		if(!(propName instanceof Attr))
			return false;
		return getAttributes().contains((Attr)propName);
	}
	
	@Override
	public boolean isPropertyValueSet(Object propName) {
		try {
			return getPropertyValue(propName) != null;
		} catch (ModelException e) {
			return false;
		}
	}
	
	@Override
	public Class<?> getPropertyNameClass() {
		return Attr.class;
	}

	@Override
	public Class<?> getPropertyBaseClass(Object propName) throws ModelException {
		if(!(propName instanceof Attr))
			throw new ModelException(ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName == null ? null : propName.getClass(), Attr.class);
		return ((Attr)propName).getBaseClass();
	}

	protected static <T> Set<T> set(Set<T> sup, T... items) {
		HashSet<T> s = new HashSet<T>(sup.size()+items.length);
		s.addAll(sup);
		s.addAll(Arrays.asList(items));
		return Collections.unmodifiableSet(s);
	}
	
	protected static <T> Set<T> set(T... items) {
		return Collections.unmodifiableSet(new HashSet<T>(Arrays.asList(items)));
	}

	@Override
	public Object getPropertyValue(Object propName) throws ModelException {
		if(!(propName instanceof Attr))
			throw new ModelException(null, this, propName, ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName);
		return getAttrValue((Attr)propName);
	}

	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case id: 
			if(value instanceof HTMLElement)
				return ((HTMLElement)value).getId();
			break;
		case cl: 
			if(value instanceof HTMLElement)
				return ((HTMLElement)value).getClassName();
			break;
		case title: 
			if(value instanceof HTMLElement)
				return ((HTMLElement)value).getTitle();
			break;
		case style: 
			if(value instanceof HTMLElement)
				return ((HTMLElement)value).getAttribute("style");
			break;
		case lang: 
			if(value instanceof HTMLElement)
				return ((HTMLElement)value).getLang();
			break;
		default:
			// nothing
		}
		throw new ModelException(null, this, attr, ErrorCode.PROPERTY_NOT_FOUND, attr);
	}

	@Override
	public void setPropertyValue(Object propName, Object obj)
			throws ModelException {
		if(!(propName instanceof Attr))
			throw new ModelException(null, this, propName, ErrorCode.INVALID_PROPERTY_NAME_CLASS, propName);
		if(!setAttrValue((Attr)propName, obj))
			throw new ModelException(null, this, propName, ErrorCode.PROPERTY_NOT_FOUND, propName);
	}
	
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
			switch(attr) {
			case id: 
				if(value instanceof HTMLElement) {
					((HTMLElement)value).setId((String)obj); 
					return true;
				}
				break;
			case cl: 
				if(value instanceof HTMLElement) {
					((HTMLElement)value).setClassName((String)obj); 
					return true;
				}
				break;
			case title: 
				if(value instanceof HTMLElement) {
					((HTMLElement)value).setTitle((String) obj);
					return true;
				}
				break;
			case style: 
				if(value instanceof HTMLElement) {
					((HTMLElement)value).setAttribute("style", (String) obj);
					return true;
				}
				break;
			case lang: 
				if(value instanceof HTMLElement) {
					((HTMLElement)value).setLang((String) obj);
					return true;
				}
				break;
			default:
				// nothing
			}
			return false;
	}

	@Override
	protected Object parsePropertyName(String propName) throws ModelException {
		try {
			return Attr.valueOf(propName.toLowerCase());
		} catch (IllegalArgumentException ex) {
			throw new ModelException(HTMLModel.TAG_NOT_FOUND, propName);
		}
	}
	
	protected HTMLDocument getDocument() {
		return ((HTMLModel)model).getDocument();
	}
}
