package taiga.parser;

import taiga.interfaces.Array;
import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.node.ARangeValue;
import taiga.parser.node.Node;

public class RangeParserContext extends ParserContext {

	private ARangeValue range;
	private ParserContext a;
	private ParserContext b;

	protected RangeParserContext(ParserContext parent, ARangeValue range) throws TaigaException {
		super(parent);
		this.range = range;
		this.a = createValueParserContext(range.getA());
		this.b = createValueParserContext(range.getB());
	}

	@Override
	public void init() throws TaigaException {
		a.init();
		b.init();
	}
	
	@Override
	public boolean hasDependency(String typeName) {
		return a.hasDependency(typeName) || b.hasDependency(typeName);
	}
	
	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		Number av = getValue(a, ic);
		Number bv = getValue(b, ic);
		if(av == null || bv == null)
			return null;
		try {
			Array array = getModel().createRange(av.intValue(), bv.intValue());
			return new PropertyValueHolder(array);
		} catch (ModelException e) {
			logAndThrow(e);
			return null;
		}
	}

	@Override
	public void initialize(InstanceContext ic) throws TaigaException {
		Number av = getValue(a, ic);
		Number bv = getValue(b, ic);
		if(av == null || bv == null)
			return;
		Property node = ic.getNode();
		if(!(node instanceof Array)) {
			logAndThrow(ErrorCode.NOT_ARRAY, node.getValueClass());
			return;
		}
		Array array = (Array) node;
		int an = av.intValue();
		int bn = bv.intValue();
		try {
			for(int n = an; n <= bn; n++)
				array.setPropertyValue(array.getNextPropName(), n);
		} catch (ModelException e) {
			logAndThrow(e);
			return;
		}
	}
	
	private Number getValue(ParserContext pc, InstanceContext ic) throws TaigaException {
		if(pc == null)
			return null;
		ValueHolder vh = pc.instantiate(ic);
		if(vh == null)
			return null;
		Object o = vh.getValue(Number.class);
		if(!(o instanceof Number)) {
			logAndThrow(o == null ? ErrorCode.NULL_VALUE_NOT_ALLOWED : ErrorCode.NOT_NUMBER);
		}
		return (Number)o;
	}
	
	@Override
	protected Node getASTNode() {
		return range;
	}

}
