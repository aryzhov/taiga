package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLBodyElement;

import taiga.interfaces.ModelException;
import taiga.model.AbstractModel;

public class BodyNode extends ContainerNode<HTMLBodyElement>{

	public BodyNode(AbstractModel model) {
		super(model, HTMLBodyElement.class);
	}

	private static final Set<Attr> ATTRS = set(ELEM_ATTRS, Attr.margin);

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}

	@Override
	protected TagType getTag() {
		return TagType.BODY;
	}
	
	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case margin: return value.getAttribute("margin");
		default: return super.getAttrValue(attr);
		}
	}
	
	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case margin: value.setAttribute("margin", (String) obj); return true;
		default: return super.setAttrValue(attr, obj);
		}
	}
}
