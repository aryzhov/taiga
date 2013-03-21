package taiga.parser;

import taiga.interfaces.ModelException;
import taiga.interfaces.TaigaException;
import taiga.parser.node.Node;
import taiga.parser.node.TFreeValue;

public class FreeValueParserContext extends ValueHandlerParserContext {

	private final TFreeValue v;

	protected FreeValueParserContext(ParserContext parent, TFreeValue v) {
		super(parent);
		this.v = v;
	}

	@Override
	protected Node getASTNode() {
		return v;
	}

	@Override
	public Object getValue(Class<?> expectedClass) throws TaigaException {
		try {
			return parseValue(v.getText(), expectedClass);
		} catch (ModelException e) {
			logAndThrow(e);
			return ERROR;
		}
	}
	
	
}
