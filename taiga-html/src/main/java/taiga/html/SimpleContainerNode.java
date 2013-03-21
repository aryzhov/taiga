package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLElement;

import taiga.model.AbstractModel;

public class SimpleContainerNode extends ContainerNode <HTMLElement> { 

	private TagType tag;

	public SimpleContainerNode(AbstractModel model, TagType tag) {
		super(model, HTMLElement.class);
		this.tag = tag;
	}

	@Override
	protected TagType getTag() {
		return tag;
	}

	@Override
	protected Set<Attr> getAttributes() {
		return ELEM_ATTRS;
	}
	
}
