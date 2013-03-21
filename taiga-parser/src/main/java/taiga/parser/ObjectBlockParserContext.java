package taiga.parser;

import taiga.interfaces.ModelException;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AObjectBlock;
import taiga.parser.node.Node;

class ObjectBlockParserContext extends ParserContext {

	private final AObjectBlock block;
	private final StatementsParserContext statements;
	

	public ObjectBlockParserContext(ParserContext parent, AObjectBlock block, TypeScope typeScope) throws TaigaException {
		super(parent);
		this.block = block;
		statements = new StatementsParserContext(parent, block.getStatements(), typeScope);
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		return new ObjectBlockValueHolder(ic);
	}
	
	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		statements.initialize(ic);
	}

	@Override
	protected Node getASTNode() {
		return block;
	}

	@Override
	public void init() throws TaigaException {
		statements.init();
	}

	@Override
	public boolean hasDependency(String typeName) {
		return statements.hasDependency(typeName);
	}

	public class ObjectBlockValueHolder implements ValueHolder {

		private InstanceContext parent;

		private ObjectBlockValueHolder(InstanceContext parent) {
			this.parent = parent;
		}

		@Override
		public Property getProperty(Class<?> expectedClass) throws TaigaException {
			try {
				taiga.interfaces.Node node = getModel().createAnonymousObjectNode();
				node.init(new EmptyParams(), parent);
				initialize(new InstanceContext(parent, node));
				return node;
			} catch (ModelException e) {
				logAndThrow(e);
				return null;
			}
		}

		@Override
		public Object getValue(Class<?> expectedClass) throws TaigaException {
			Property prop = getProperty(expectedClass);
			try {
				return prop == null ? null : prop.getValue(expectedClass);
			} catch (ModelException e) {
				logAndThrow(e);
				return null;
			}
		}

	}


}
