package taiga.parser;

import taiga.interfaces.TaigaException;
import taiga.parser.node.Node;
import taiga.parser.node.PValue;

public class ValueParserContext extends ParserContext {

	private PValue value;
	protected ParserContext child;
	
	protected ValueParserContext(ParserContext parent, PValue value, TypeScope typeScope) throws TaigaException {
		super(parent);
		this.value = value;
		child = createValueParserContext(value);
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		if(child == null)
			return null;
		return child.instantiate(ic);
	}

	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		if(child != null)
			child.initialize(ic);
	}

	@Override
	protected Node getASTNode() {
		return value;
	}

	@Override
	public void init() throws TaigaException {
		if(child != null)
			child.init();
	}
	
	@Override
	public boolean hasDependency(String typeName) {
		if(child != null)
			return child.hasDependency(typeName);
		else
			return false;
	}
}
