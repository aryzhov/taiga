package taiga.parser;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Params;
import taiga.interfaces.Property;
import taiga.interfaces.Signature;
import taiga.interfaces.StringUtils;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AConstIdent;
import taiga.parser.node.AIdIdent;
import taiga.parser.node.AModel;
import taiga.parser.node.AName;
import taiga.parser.node.AObject;
import taiga.parser.node.AParam;
import taiga.parser.node.Node;
import taiga.parser.node.PIdent;
import taiga.parser.node.PParam;

class ObjectParserContext extends TypeScopeParserContext implements Iterable<Object> {

	private final AObject object;
	private final BlocksParserContext blocks;
	private final ParserContext[] values;
	private final Model model;
	private final String typeName;
	private ParserContext type = null;
	private Object modelTypeName = null;
	private boolean initialized = false;
	
	protected ObjectParserContext(ParserContext parent, AObject object) throws TaigaException {
		super(parent);
		this.object = object;
		this.typeName = parseName((AName) object.getName());
		if(object.getModel() != null) {
			String modelName = parseId(((AModel)object.getModel()).getId());
			try {
				model = parent.getOwner().getModel(modelName);
			} catch (ModelException e) {
				throw new TaigaException(logAndThrow(e));
			}
		} else {
			model = parent.getModel();
		}
		blocks = new BlocksParserContext(this, object.getBlocks(), getTypeScope());
		ParserContext[] values0 = new ParserContext[object.getParams().size()];
		int idx = 0;
		for(PParam p: object.getParams()) {
			AParam param = (AParam) p;
			if(param.getParamName() != null) {
				logAndThrow(ErrorCode.NOT_IMPLEMENTED);
			}
			values0[idx++] = createValueParserContext(param.getValue());
		}
		this.values = values0;
	}
	
	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		if(type instanceof ValueHolder)
			return (ValueHolder) type;
		else
			return new ObjectValueHolder(ic);
	}
			
	private class ObjectParams implements Params {
		final ValueHolder[] paramValues = new ValueHolder[values.length];

		ObjectParams(InstanceContext ic) throws TaigaException {
			for(int i = 0; i < values.length; i++) {
				ParserContext param = values[i];
				ValueHolder paramValue = param.instantiate(ic);
				if(paramValue == null) {
					logAndThrow(ErrorCode.UNRESOLVED, i, modelTypeName);
				} else {
					paramValues[i] = paramValue;
				}
			}
		}
		
		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object[] getParameterValues(Signature signature) throws ModelException {
			Object[] result = new Object[signature.size()];
			for(int i = 0; i < paramValues.length; i++)
				result[i] = getParameter(i, signature.get(i));
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getParameter(int idx, Class<T> baseClass) throws ModelException {
			if(idx < 0 || idx >= paramValues.length)
				throw new ModelException(ErrorCode.INVALID_PARAM_INDEX, idx);			
			try {
				return (T) paramValues[idx].getValue(baseClass);
			} catch (TaigaException e) {
				throw new ModelException(e, ErrorCode.OBJECT_PARAM_ERROR, idx);
			}
		}

		@Override
		public <S extends Signature> S selectSignature(S... signatures) throws ModelException { 
			for(S s: signatures)
				if(s.size() == paramValues.length)
					return s;
			throw new ModelException(ErrorCode.PARAM_SIGNATURE_MISMATCH); 
		}

		
	}
	
	private class ObjectInstanceContext extends InstanceContext {

		private ObjectParams params;

		ObjectInstanceContext(InstanceContext parent, Property node, ObjectParams params) throws TaigaException {
			super(parent, node);
			this.params = params;
		}
		
		@Override
		public ValueHolder resolve(ParserContext pc) {
			if(pc instanceof ParamDefParserContext && pc.parent == type) {
				int idx = ((ParamDefParserContext)pc).getParamIndex();
				if(idx < params.paramValues.length)
					return params.paramValues[idx];
				else
					return null;
			} else {
				return super.resolve(pc);
			}
		}

	}
	
	private class ObjectValueHolder implements ValueHolder {

		private InstanceContext ic;

		private ObjectValueHolder(InstanceContext ic) throws TaigaException {
			this.ic = ic;
		}

		@Override
		public Property getProperty(Class<?> expectedClass) throws TaigaException {
			if(!initialized)
				throw new TaigaException(logAndThrow(ErrorCode.ASSERTION_ERROR));
			
			boolean isNode = blocks.hasObjectBlocks() || blocks.hasArrayBlocks();
			boolean isArray = isNode && blocks.hasArrayBlocks();
			ObjectParams params = new ObjectParams(ic);
			
			try {
				final ValueHolder result;
				final Property value;
				if(type != null) {
					if(type instanceof TypeParserContext)
						result = type.instantiate(new ObjectInstanceContext(ic, ic.getNode(), params));
					else
						result = ic.resolve(type);
					if(result == null) {
						logAndThrow(ErrorCode.TYPE_NOT_FOUND, typeName);
						return null;
					}
					value = result.getProperty(expectedClass);
					if(value == null)
						return null;
				} else {
					if(modelTypeName != null) {
						value = getModel().createNode((taiga.interfaces.Node) ic.getNode(), modelTypeName); 
						if(value == null)
							logAndThrow(ErrorCode.TYPE_NOT_FOUND, modelTypeName);
					} else if(Object.class.equals(expectedClass)) {
						value = isArray ?  getModel().createAnonymousArrayNode() : getModel().createAnonymousObjectNode();
					} else {
						value = getModel().createNode(expectedClass);
					}
					if(value == null)
						return null;
					result = new PropertyValueHolder(value);
				}

				
				// TODO: replace with empty params object
				if(!value.isValueSet())
					try {
						value.init(params, ic);
					} catch (ModelException e) {
						logAndThrow(e);
						return null;
					}
				initialize(new ObjectInstanceContext(ic, value, params));
				return value;
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
	
	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		blocks.initialize(ic);
	}

	@Override
	protected Node getASTNode() {
		return object;
	}

	@Override
	public void init() throws TaigaException {
		if(typeName != null) {
			type = parent.resolve(typeName, NameType.TYPE);
			if(type instanceof TypeParserContext) {
				TypeParserContext tpc = (TypeParserContext) type;
				int n = tpc.getParamDefs().length; 
				if(n > values.length) {
					for(int i = values.length; i < n; i++)
						logAndThrow(ErrorCode.MISSING_PARAM, tpc.getParamDefs()[i].getParamName(), modelTypeName);
				} else if(n < values.length) {
					logAndThrow(ErrorCode.TOO_MANY_PARAMS, modelTypeName);
				}
			} else if(type == null) {
				try {
					modelTypeName = getModel().parseTypeName(typeName, this);
					if(modelTypeName == null) {
						// TODO: check the call didn't contain parenthesis
						if(values.length == 0) {
							type = parent.resolve(typeName, NameType.VALUE);
							if(type == null)
								logAndThrow(ErrorCode.CANNOT_RESOLVE_NAME, typeName);
						} else {
							logAndThrow(ErrorCode.TYPE_NOT_FOUND, typeName);
						}
					} 
				} catch (ModelException e) {
					logAndThrow(e);
				}
			}
		}
		initialized = true;
		for(ParserContext pc: values)
			pc.init();
		super.init();
		blocks.init();
	}

	private static class TypeScopeIterator implements Iterator<Object> {
		
		private ObjectParserContext next;
		
		public TypeScopeIterator(ObjectParserContext opc) {
			next = findNext(opc);
		}
		
		private static ObjectParserContext findNext(ObjectParserContext last) {
			for(ParserContext pc = last.parent; pc != null; pc = pc.parent) {
				if(pc instanceof ObjectParserContext) {
					ObjectParserContext opc = (ObjectParserContext) pc;
					assert opc.initialized;
					if(opc.model == last.model && opc.modelTypeName != null)
						return opc;
				}
			}
			return null;
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public Object next() {
			if(next == null)
				throw new NoSuchElementException();
			Object result = next.modelTypeName; 
			next = findNext(next);
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	@Override
	protected ParserContext doResolve(String name) {
		ParserContext pc = super.doResolve(name);
		if(pc != null)
			return pc;
		if(type instanceof TypeParserContext) {
			return ((TypeParserContext)type).resolveLocal(name);
		} else
			return null;
	}

	@Override
	public boolean hasDependency(String typeName) {
		if(StringUtils.equals(this.typeName, typeName, false) || super.hasDependency(typeName) ||
			blocks.hasDependency(typeName))
			return true;
		for(ParserContext pc: values)
			if(pc.hasDependency(typeName))
				return true;
		return false;
	}

	/** Parses property path */
	protected String parseName(AName name) throws TaigaException {
		if(name == null)
			return null;
		List<PIdent> parts = name.getParts();
		if(parts.size() == 1) 
			return identToString(parts.get(0));
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(PIdent id: name.getParts()) {
			String s = identToString(id);
			if(s == null)
				return null;
			if(!first)
				sb.append(".");
			else
				first = false;
			sb.append(s);
		}
		return sb.toString();
	}
	
	private String identToString(PIdent ident) throws TaigaException {
		if(ident instanceof AIdIdent) {
			return parseId(((AIdIdent)ident).getId());
		} else if(ident instanceof AConstIdent) {
			return (String) parseConstant(((AConstIdent)ident).getConstant(), String.class);
		}
		else {
			logAndThrow(ErrorCode.NOT_SUPPORTED, ident);
			return null;
		}
	}

	@Override
	public Iterator<Object> iterator() {
		return new TypeScopeIterator(this);
	}
}
