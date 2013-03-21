package taiga.parser;

import java.util.ArrayList;
import java.util.List;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AArrayBlock;
import taiga.parser.node.Node;
import taiga.parser.node.PValue;

public class ArrayBlockParserContext extends ParserContext {

	private final AArrayBlock block;
	private final List<ParserContext> values;

	public ArrayBlockParserContext(ParserContext parent, AArrayBlock block) throws TaigaException {
		super(parent);
		this.block = block;
		this.values = new ArrayList<ParserContext>();
		for(PValue value: block.getValues()) {
			ParserContext pc = createValueParserContext(value);
			if(pc != null)
				values.add(pc);
		}
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		switch(ic.instantiateMode) {
		case VALUE:
			if(values.size() != 1 || values.get(0) instanceof LoopParserContext) {
				logAndThrow(values.size() == 0 ? ErrorCode.VALUE_NOT_DEFINED : ErrorCode.SINGLE_VALUE_EXPECTED);
				return null;
			}
			return values.get(0).instantiate(ic);
		case MAP:
			logAndThrow(ErrorCode.NOT_SUPPORTED);
			return null;
		default:
			return new ArrayBlockValueHolder(ic);
		}
	}
	
	
	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		Property node = ic.getNode();
		switch(ic.instantiateMode) {
		case VALUE:
			if(values.size() != 1 || values.get(0) instanceof LoopParserContext) {
				logAndThrow(values.size() == 0 ? ErrorCode.VALUE_NOT_DEFINED : ErrorCode.SINGLE_VALUE_EXPECTED);
				return;
			}
			values.get(0).initialize(new InstanceContext(ic.parent, ic.node));
			break;
		case MAP:
			logAndThrow(ErrorCode.NOT_SUPPORTED);
			return;
		default:
			if(!(node instanceof Array)) {
				logAndThrow(ErrorCode.NOT_ARRAY, node.getValueClass());
				return;
			}
			Array array = (Array) node;
			for(ParserContext pc: values) {
				if(pc instanceof LoopParserContext)
					pc.initialize(ic);
				else {
					try {
						Object propName = array.getNextPropName();
						ValueHolder vh = pc.instantiate(ic);
						if(vh != null) { 
							Property p = vh.getProperty(array.getPropertyBaseClass(propName));
							if(p != null) 
								p.assignTo(array, propName);
						}
					} catch (ModelException e) {
						logAndThrow(e);
					}
				}
			}
		}
	}

	@Override
	protected Node getASTNode() {
		return block;
	}

	@Override
	public void init() throws TaigaException {
		for(ParserContext pc: values)
			pc.init();
	}
	
	@Override
	public boolean hasDependency(String typeName) {
		for(ParserContext pc: values)
			if(pc.hasDependency(typeName))
				return true;
		return false;
	}

	public class ArrayBlockValueHolder implements ValueHolder {

		private InstanceContext parent;

		private ArrayBlockValueHolder(InstanceContext parent) {
			this.parent = parent;
		}

		@Override
		public Property getProperty(Class<?> expectedClass) throws TaigaException {
			try {
				taiga.interfaces.Node node = getModel().createAnonymousArrayNode();
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

	public Property createNodeForObject(Object obj) throws TaigaException {
		if(values.size() != 1 || values.get(0) instanceof LoopParserContext) {
			logAndThrow(values.size() == 0 ? ErrorCode.VALUE_NOT_DEFINED : ErrorCode.SINGLE_VALUE_EXPECTED);
			return null;
		}
		try {
			Property prop = values.get(0).getModel().createNode(obj.getClass());
			prop.setValue(obj);
			return prop;
		} catch (ModelException e) {
			logAndThrow(e);
			return null;
		}
		
	}

}
