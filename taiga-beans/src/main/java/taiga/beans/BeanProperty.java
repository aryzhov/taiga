package taiga.beans;

import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.interfaces.Signature;
import taiga.model.AbstractProperty;

public class BeanProperty extends AbstractProperty<Object> {

	public BeanProperty(BeanModel model, Class<?> baseClass) {
		super(model, baseClass);
	}

	@Override
	protected Object createInitValue(Params params, Iterable<Node> scope) throws ModelException {
		params.selectSignature(new Signature(baseClass));
		return params.getParameter(0, baseClass);
	}
	
}
