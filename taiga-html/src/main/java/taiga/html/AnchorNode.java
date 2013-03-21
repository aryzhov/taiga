package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLAnchorElement;

import taiga.interfaces.ModelException;
import taiga.model.AbstractModel;

public class AnchorNode extends ContainerNode<HTMLAnchorElement> {

	private static final Set<Attr> ATTRS = set(ELEM_ATTRS, Attr.accesskey, Attr.tabindex, Attr.type, Attr.name, Attr.target);

	public AnchorNode(AbstractModel model) {
		super(model, HTMLAnchorElement.class);
	}
	
	@Override
	protected TagType getTag() {
		return TagType.A;
	}

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}

	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case href: return value.getHref();
		case accesskey: return value.getAccessKey();
		case tabindex: return value.getTabIndex();
		case type: return value.getType();
		case name: return value.getName();
		case target: return value.getTarget();
		default: return super.getAttrValue(attr);
		}
	}
	
	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case href: value.setHref((String) obj); return true;
		case accesskey: value.setAccessKey((String) obj); return true;
		case tabindex: value.setTabIndex((Integer)obj); return true;
		case type: value.setAttribute("type", (String)obj); return true;
		case name: value.setName((String) obj); return true;
		case target: value.setTarget((String) obj); return true;
		default: return super.setAttrValue(attr, obj);
		}
	}
}
