package taiga.parser;

import java.util.Collection;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;
import taiga.interfaces.StringUtils;
import taiga.interfaces.TaigaException;
import taiga.parser.node.ALoopValue;
import taiga.parser.node.ALoop;
import taiga.parser.node.AObject;

public class LoopParserContext extends ParserContext {

	private ALoopValue av;
	private ObjectParserContext srcArray;
	private ParserContext value;
	private String loopVar;

	protected LoopParserContext(ParserContext parent, ALoopValue av) throws TaigaException {
		super(parent);
		this.av = av;
		ALoop loop = (ALoop) av.getLoop();
		this.srcArray = new ObjectParserContext(this, (AObject) loop.getObject());
		this.loopVar = parseId(loop.getLoopVar());
		this.value = createValueParserContext(av.getValue());
	}

	@Override
	protected taiga.parser.node.Node getASTNode() {
		return av;
	}
	
	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		initialize(ic);
		return null;
	}

	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		Array destArray = getArrayNode(ic);
		if(destArray == null)
			return;
		ValueHolder srcValue = srcArray.instantiate(ic);
		if(srcValue == null)
			return;
		Property srcProp = srcValue.getProperty(Collection.class);
		if(!(srcProp instanceof Array)) {
			logAndThrow(ErrorCode.NOT_ARRAY, srcProp.getValueClass(), srcArray.getASTNode());
			return;
		}
		Array loopArray = (Array) srcProp;  
		try {
			for (Property p : loopArray.elements()) {
				final Property prop = p;
				Object propName = destArray.getNextPropName();
				ValueHolder destValue = value.instantiate(new LoopInstanceContext(ic, prop));
				if (destValue != null) {
					Property p2 = destValue.getProperty(destArray.getPropertyBaseClass(propName));
					if (p2 != null)
						p2.assignTo(destArray, propName);
				}
			}
		} catch (ModelException e) {
			logAndThrow(e);
		}
	}

	private Array getArrayNode(InstanceContext ic) throws TaigaException {
		for(Node n: ic)
			if(n instanceof Array)
				return (Array) n;
		logAndThrow(ErrorCode.LOOP_OUTSIDE_ARRAY);
		return null;
	}

	@Override
	public void init() throws TaigaException {
		srcArray.init();
		value.init();
	}
	
	@Override
	protected ParserContext doResolve(String name) {
		if (StringUtils.equals(name, loopVar, false))
			return this;
		else
			return super.doResolve(name);
	}
	
	@Override
	public boolean hasDependency(String typeName) {
		return srcArray.hasDependency(typeName) || value.hasDependency(typeName); 
	}

	private final class LoopInstanceContext extends InstanceContext {
		private final Property arrayElement;

		private LoopInstanceContext(InstanceContext parent, Property arrayElement) {
			super(parent, parent.node);
			this.arrayElement = arrayElement;
		}

		@Override
		public ValueHolder resolve(ParserContext pc) {
			if (pc == LoopParserContext.this)
				return new PropertyValueHolder(arrayElement);
			else
				return super.resolve(pc);
		}
	}

}
