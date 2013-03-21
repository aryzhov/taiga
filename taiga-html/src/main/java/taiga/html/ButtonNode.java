package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLButtonElement;

import taiga.interfaces.ModelException;
import taiga.interfaces.StringUtils;
import taiga.model.AbstractModel;

public class ButtonNode extends ContainerNode<HTMLButtonElement> {

	private static final Set<Attr> ATTRS = set(ELEM_ATTRS, Attr.accesskey, Attr.tabindex, Attr.type, Attr.value, Attr.disabled, Attr.name);

	public ButtonNode(AbstractModel model) {
		super(model, HTMLButtonElement.class);
	}

	@Override
	protected TagType getTag() {
		return TagType.BUTTON;
	}

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}
	
	@Override
	protected Object getAttrValue(Attr attr) throws ModelException {
		switch(attr) {
		case accesskey: return value.getAccessKey();
		case tabindex: return value.getTabIndex();
		case type: return value.getType();
		case value: return value.getValue();
		case disabled: return value.getDisabled() ? "disabled" : null;
		case name: return value.getName();
		default: return super.getAttrValue(attr);
		}
	}
	
	@Override
	protected boolean setAttrValue(Attr attr, Object obj) throws ModelException {
		switch(attr) {
		case accesskey: value.setAccessKey((String) obj); return true;
		case tabindex: value.setTabIndex((Integer)obj); return true;
		case type: value.setAttribute("type", (String)obj); return true;
		case value: value.setValue((String)obj); return true;
		case disabled: value.setDisabled(StringUtils.equals("disabled", (String)obj, true)); return true;
		case name: value.setName((String) obj); return true;
		default: return super.setAttrValue(attr, obj);
		}
	}
}
