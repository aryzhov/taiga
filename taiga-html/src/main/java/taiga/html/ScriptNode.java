package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLScriptElement;

import taiga.interfaces.ModelException;
import taiga.model.AbstractModel;

public class ScriptNode extends ContainerNode<HTMLScriptElement> {

	private static final Set<Attr> ATTRS = set(ELEM_ATTRS, Attr.type, Attr.src);

	public ScriptNode(AbstractModel model) {
		super(model, HTMLScriptElement.class);
	}

	@Override
	protected TagType getTag() {
		return TagType.SCRIPT;
	}

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}
	
	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case src: return value.getSrc();
		case type: return value.getType();
		default: return super.getAttrValue(attr);
		}
	}
	
	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case src: value.setSrc((String) obj); return true;
		case type: value.setType((String) obj); return true;
		default: return super.setAttrValue(attr, obj);
		}
	}
}
