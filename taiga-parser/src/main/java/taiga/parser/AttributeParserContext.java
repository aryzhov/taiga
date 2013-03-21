package taiga.parser;

import taiga.interfaces.TaigaException;
import taiga.parser.node.Node;

public class AttributeParserContext extends ParserContext {

	private String attributeName;
	
	protected AttributeParserContext(ParserContext parent, String attributeName) {
		super(parent);
		this.attributeName = attributeName;
	}

	@Override
	protected Node getASTNode() {
		return null;
	}

	public String getAttributeName() {
		return attributeName;
	}

	@Override
	public boolean hasDependency(String typeName) {
		return false;
	}

	@Override
	public void init() throws TaigaException {
	}
}
