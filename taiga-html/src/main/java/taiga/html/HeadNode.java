package taiga.html;

import java.util.Set;

import org.w3c.dom.html.HTMLHeadElement;

import taiga.model.AbstractModel;

public class HeadNode extends ContainerNode<HTMLHeadElement>{

	public HeadNode(AbstractModel model) {
		super(model, HTMLHeadElement.class);
	}

	private static final Set<Attr> ATTRS = ELEM_ATTRS;

	@Override
	protected Set<Attr> getAttributes() {
		return ATTRS;
	}

	@Override
	protected TagType getTag() {
		return TagType.HEAD;
	}
}
