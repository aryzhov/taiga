package taiga.parser;

import taiga.interfaces.ModelException;
import taiga.interfaces.TaigaException;
import taiga.parser.node.Node;
import taiga.parser.node.PConstant;

public class ConstantParserContext extends ValueHandlerParserContext {

	private final PConstant constant;
	private final Object cachedValue;
	
	protected ConstantParserContext(ParserContext parent, PConstant constant) throws TaigaException {
		super(parent);
		this.constant = constant;
		cachedValue = parseConstant(constant, Object.class);
	}

	@Override
	protected Node getASTNode() {
		return constant;
	}

	@Override
	public Object getValue(Class<?> expectedClass) throws TaigaException {
		if(cachedValue == ERROR)
			return null;
		if(isAssignableFrom(expectedClass, cachedValue))
			return cachedValue;
		try {
			return getModel().convert(cachedValue, expectedClass);
		} catch (ModelException e) {
			logAndThrow(e);
			return null;
		}
	}
	
}
