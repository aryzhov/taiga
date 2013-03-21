package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLLinkElement;

import taiga.interfaces.ModelException;
import taiga.model.AbstractModel;

public class LinkNode extends TagNode <HTMLLinkElement>{

	private static final Set<Attr> ATTRS = set(ELEM_ATTRS, Attr.type, Attr.rel, Attr.href);

	public LinkNode(AbstractModel model) {
		super(model, HTMLLinkElement.class);
	}

	@Override
	protected TagType getTag() {
		return TagType.LINK;
	}

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}
	
	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case href: return value.getHref();
		case rel: return value.getRel();
		case type: return value.getType();
		default: return super.getAttrValue(attr);
		}
	}
	
	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case href: value.setHref((String) obj); return true;
		case rel: value.setRel((String) obj); return true;
		case type: value.setType((String) obj); return true;
		default: return super.setAttrValue(attr, obj);
		}
	}
	
}
