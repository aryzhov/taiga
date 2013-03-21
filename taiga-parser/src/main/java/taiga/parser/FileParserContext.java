package taiga.parser;

import java.util.Map;

import taiga.interfaces.Model;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.InstanceContext.Mode;
import taiga.parser.node.ARoot;
import taiga.parser.node.Node;
import taiga.parser.node.PRoot;
import taiga.parser.node.Token;

class FileParserContext extends TypeScopeParserContext {

	private final String fileName;
	private final StatementsParserContext child;
	private final Map<Node, Token> tokenMap;
	private final Model defaultModel;
	
	public FileParserContext(ParserContext parent, String fileName, PRoot g, Model defaultModel) throws TaigaException {
		super(parent);
		this.fileName = fileName;
		this.defaultModel = defaultModel;
		
		TokenMapper tm = new TokenMapper();
		g.apply(tm);
		tokenMap = tm.getMap();
		
		child = new StatementsParserContext(this, ((ARoot)g).getStatements(), getTypeScope());
	}

	@Override
	public Model getModel() {
		if(defaultModel != null)
			return defaultModel;
		else
			return super.getModel();
	}
	
	@Override
	public ValueHolder instantiate(InstanceContext ic)
			throws TaigaException {
		return child.instantiate(ic);
	}
	
	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		child.initialize(ic);
	}

	@Override
	protected Token getFirstToken(Node node) {
		if(node instanceof Token)
			return (Token) node;
		else
			return tokenMap.get(node);
	}

	@Override
	protected Node getASTNode() {
		return child.getASTNode();
	}

	@Override
	public void init() throws TaigaException {
		super.init();
		child.init();
	}

	public boolean supportsMode(Mode mode) {
		return child.supportsMode(mode);
	}

	public Property createNodeForObject(Object obj) throws TaigaException {
		return child.createNodeForObject(obj);
	}
}
