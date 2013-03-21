package taiga.parser;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AParamDef;
import taiga.parser.node.Node;

class ParamDefParserContext extends ParserContext {

	private final String paramName;
	private final AParamDef param;
	private final int paramIndex; 
	
	protected ParamDefParserContext(TypeParserContext parent, AParamDef param, int idx) {
		super(parent);
		this.param = param;
		paramName = parseId(param.getId());
		this.paramIndex = idx;
	}

	public String getParamName() {
		return paramName;
	}
	
	public int getParamIndex() {
		return paramIndex;
	}
	
	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		ValueHolder value = ic.resolve(this);
		if(value == null) {
			logAndThrow(ErrorCode.UNRESOLVED, paramName, ((TypeParserContext)parent).getTypeName());
			return null;
		}
		return value;
	}

	@Override
	protected Node getASTNode() {
		return param;
	}

	@Override
	public boolean hasDependency(String typeName) {
		return false;
	}

	@Override
	public void init() throws TaigaException {
	}

}
