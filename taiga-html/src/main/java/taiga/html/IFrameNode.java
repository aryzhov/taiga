package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLIFrameElement;

import taiga.interfaces.ModelException;
import taiga.model.AbstractModel;

public class IFrameNode extends TagNode<HTMLIFrameElement> {

	private static final Set<Attr> ATTRS = set(ELEM_ATTRS, Attr.src, Attr.tabindex);

	public IFrameNode(AbstractModel model) {
		super(model, HTMLIFrameElement.class);
	}

	@Override
	protected TagType getTag() {
		return TagType.IFRAME;
	}

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}
	
	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case src: return value.getSrc();
		case tabindex: return strToInt(value.getAttribute("tabindex"));
		default: return super.getAttrValue(attr);
		}
	}
	
	protected String intToStr(Integer i) {
		return i == null ? null : i.toString();
	}
	
	protected Integer strToInt(String s) {
		if(s == null || s.trim().isEmpty())
			return null;
		try {
			return Integer.parseInt(s.trim());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case src: value.setSrc((String) obj); return true;
		case tabindex: value.setAttribute("tabindex", intToStr((Integer) obj)); return true;
		default: return super.setAttrValue(attr, obj);
		}
	}
}
