package taiga.html;

import java.util.Set;

import org.w3c.dom.Node;

import taiga.model.AbstractModel;

public class NoscriptNode extends ContainerNode<Node> {

	private static final Set<Attr> ATTRS = set();

	public NoscriptNode(AbstractModel model) {
		super(model, Node.class);
	}

	public Set<Attr> getAttributes() {
		return ATTRS;
	}

	@Override
	protected TagType getTag() {
		return TagType.NOSCRIPT;
	}


}
