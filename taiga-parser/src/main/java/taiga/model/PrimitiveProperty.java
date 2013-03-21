package taiga.model;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;

public class PrimitiveProperty extends AbstractProperty<Object> {

	public PrimitiveProperty(AbstractModel model, Class<?> baseClass) {
		super(model, baseClass);
	}

	@Override
	protected Object createInitValue(Params params, Iterable<Node> scope) throws ModelException {
		throw new ModelException(ErrorCode.NOT_SUPPORTED);
	}

}
