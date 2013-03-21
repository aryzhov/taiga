package taiga.parser;

import taiga.interfaces.TaigaException;
import taiga.parser.node.AInitialization;
import taiga.parser.node.Node;

public class InitializationParserContext extends TypeScopeParserContext {

	private final AInitialization init;
	private final BlocksParserContext blocks;

	protected InitializationParserContext(ParserContext parent, AInitialization init) throws TaigaException {
		super(parent);
		this.init = init;
		blocks = new BlocksParserContext(this, init.getBlocks(), getTypeScope());
	}

	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		blocks.initialize(ic);
	}

	@Override
	protected Node getASTNode() {
		return init;
	}
	
	@Override
	public void init() throws TaigaException {
		super.init();
		blocks.init();
	}
}
