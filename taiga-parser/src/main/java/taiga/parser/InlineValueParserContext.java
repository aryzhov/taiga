package taiga.parser;

import taiga.interfaces.ModelException;
import taiga.interfaces.TaigaException;
import taiga.parser.node.Node;
import taiga.parser.node.TInlineValue;

public class InlineValueParserContext extends ValueHandlerParserContext {

	private final TInlineValue v;
	
	protected InlineValueParserContext(ParserContext parent, TInlineValue v) {
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
