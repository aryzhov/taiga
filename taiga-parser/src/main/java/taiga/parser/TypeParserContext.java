package taiga.parser;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.StringUtils;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AParamDef;
import taiga.parser.node.ATypeDef;
import taiga.parser.node.Node;
import taiga.parser.node.PParamDef;

class TypeParserContext extends ParserContext {

	private final ATypeDef typeDef;
	private final String typeName;
	private final ParserContext value;
	private final ParamDefParserContext[] paramDefs;
	private boolean initialized = false;
	
	protected TypeParserContext(ParserContext parent, ATypeDef typeDef) throws TaigaException {
		super(parent);
		this.typeDef = typeDef;
		this.typeName = parseId(typeDef.getTypeName());
		value = createValueParserContext(typeDef.getValue());
		ParamDefParserContext[] pd = new ParamDefParserContext[typeDef.getParamDef().size()];
		int idx = 0;
		for(PParamDef paramDef: typeDef.getParamDef()) {
			ParamDefParserContext pc = new ParamDefParserContext(this, (AParamDef) paramDef, idx);
			pd[idx++] = pc;
		}
		paramDefs = pd;
	}

	public String getTypeName() {
		return typeName;
	}

	public ParserContext getValue() {
		return value;
	}

	public ParamDefParserContext[] getParamDefs() {
		return paramDefs;
	}
	
	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		final ValueHolder[] values = new ValueHolder[paramDefs.length];
		for(int i = 0; i < values.length; i++) {
			values[i] = ic.resolve(paramDefs[i]);
			if(values[i] == null) {
				logAndThrow(ErrorCode.UNRESOLVED, paramDefs[i].getParamName(), typeName);
			}
		}
		return value.instantiate(new ParameterValueInstanceContext(ic, values));
	}

	@Override
	protected Node getASTNode() {
		return typeDef;
	}

	@Override
	public void init() throws TaigaException {
		value.init();
		initialized = true;
	}

	@Override
	protected ParserContext doResolve(String name) {
		for(ParamDefParserContext pc: paramDefs)
			if(StringUtils.equals(pc.getParamName(), name, false))
				return pc;
		return null;
	}
	
	public ParserContext resolveLocal(String name) {
		if(value instanceof ObjectParserContext)
			return value.doResolve(name);
		else
			return null;
	}

	boolean isInitialized() {
		return initialized;
	}

	@Override
	public boolean hasDependency(String typeName) {
		return value.hasDependency(typeName);
	}

	private final class ParameterValueInstanceContext extends InstanceContext {
		private final ValueHolder[] values;

		private ParameterValueInstanceContext(InstanceContext parent, ValueHolder[] values) {
			super(parent, parent.getNode());
			this.values = values;
		}

		@Override
		public ValueHolder resolve(ParserContext pc) {
			if(pc instanceof ParamDefParserContext) {
				ParamDefParserContext pd = (ParamDefParserContext) pc;
				if(pd.parent == TypeParserContext.this)
					return values[pd.getParamIndex()];
			}
			return super.resolve(pc);
		}
	}

}
