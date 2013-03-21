package taiga.html;

import org.w3c.dom.Node;

import taiga.interfaces.ModelException;
import taiga.interfaces.Params;
import taiga.model.AbstractModel;

public abstract class TagNode<N extends Node> extends ElementNode<N> {

	public TagNode(AbstractModel model, Class<? extends N> baseClass) {
		super(model, baseClass);
	}

	protected abstract TagType getTag();

	@SuppressWarnings("unchecked")
	@Override
	protected N createInitValue(Params params, Iterable<taiga.interfaces.Node> scope) throws ModelException {
		// TODO: reject any params
		N result = (N) getDocument().createElement(getTag().name());
		return result;
	}
	
}
