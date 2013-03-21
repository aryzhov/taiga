package taiga.html;

import java.util.Set;

import org.w3c.dom.Node;

import taiga.interfaces.ModelException;
import taiga.interfaces.Params;
import taiga.model.AbstractModel;

public class TextNode extends ElementNode<Node> {

	public TextNode(AbstractModel model) {
		super(model, Node.class);
	}

	private static final Set<Attr> ATTRS = set(Attr.text);
	
	@Override
	public Set<Attr> getAttributes() {
		return ATTRS;
	}

	@Override
	protected Node createInitValue(Params params, Iterable<taiga.interfaces.Node> scope) throws ModelException {
		return getDocument().createTextNode(params.getParameter(0, String.class));
	}


}
