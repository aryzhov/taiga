package taiga.html;

import org.apache.html.dom.HTMLDocumentImpl;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLDocument;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Property;
import taiga.interfaces.StringUtils;
import taiga.model.AbstractNode;
import taiga.model.AbstractProperty;
import taiga.model.CoreModel;

public class HTMLModel extends CoreModel {

	static final ErrorCode TAG_NOT_FOUND = new ErrorCode(100, "Tag not found: {0}");
	static final ErrorCode ATTR_NOT_FOUND = new ErrorCode(101, "Attribute not found: {0}");
	private HTMLDocument document = null;
	
	@Override
	public Property createNode(taiga.interfaces.Node parent, Object typeName) throws ModelException {
		TagType tt = (TagType) typeName;
		if(document == null || tt == TagType.HTML)
			document = new HTMLDocumentImpl();
		switch(tt) {
		case HTML: return new DocumentNode(this);
		case A: return new AnchorNode(this);
		case LINK: return new LinkNode(this);
		case SCRIPT: return new ScriptNode(this);
		case NOSCRIPT: return new NoscriptNode(this);
		case IFRAME: return new IFrameNode(this);
		case BUTTON: return new ButtonNode(this);
		default: return new SimpleContainerNode(this, tt);
		}
	}

	@Override
	public AbstractProperty<?> createProperty(Class<?> cl, AbstractNode<?> node, Object propName)
			throws ModelException {
		if(propName instanceof Attr) {
			switch((Attr)propName) {
			case head: return new HeadNode(this);
			case body: return new BodyNode(this);
			default: 
			}
		}
		return new PrimitiveProperty(this, cl);
	}
	
	public HTMLDocument getDocument() {
		return document;
	}

	@Override
	public Object parseValue(String value, Class<?> expectedClass) throws ModelException {
		if(expectedClass == Attr.class) {
			try {
				return Attr.valueOf(value.toLowerCase());
			} catch (IllegalArgumentException ex) {
				throw new ModelException(ATTR_NOT_FOUND, value);
			}
		} else if(expectedClass.isAssignableFrom(Node.class)) {
			return document.createTextNode(value);
		} else {
			return super.parseValue(value, expectedClass);
		}
	}

	@Override
	public Object convert(Object value, Class<?> expectedClass) throws ModelException {
		if(expectedClass == Node.class)
			return document.createTextNode(StringUtils.toString(value));
		else
			return super.convert(value, expectedClass);
	}
	
	@Override
	public Object parseTypeName(String name, Iterable<Object> scope) throws ModelException {
		try {
			return TagType.valueOf(name.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	@Override
	public boolean isAssignableFrom(Class<?> dest, Object src) {
		return dest.isAssignableFrom(src.getClass());
	}
	
	@Override
	public boolean isInstance(Class<?> cl, Object obj) {
		return cl.isInstance(obj);
	}
}
