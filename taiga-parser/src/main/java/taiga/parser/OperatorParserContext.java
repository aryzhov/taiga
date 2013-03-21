package taiga.parser;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.Operator;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.node.Node;
import taiga.parser.node.PValue;

public class OperatorParserContext extends ParserContext {

	private final Node astNode;
	private final Operator op;
	private ParserContext a;
	private ParserContext b;

	protected OperatorParserContext(ParserContext parent, Node astNode, PValue a, PValue b, Operator op) throws TaigaException {
		super(parent);
		this.astNode = astNode;
		this.a = createValueParserContext(a);
		this.b = b == null ? null : createValueParserContext(b);
		this.op = op;
	}

	@Override
	protected Node getASTNode() {
		return astNode;
	}

	@Override
	public void init() throws TaigaException {
		a.init();
		if(b != null)
			b.init();
	}

	@Override
	public boolean hasDependency(String typeName) {
		return a.hasDependency(typeName) || (b != null && b.hasDependency(typeName)); 
	}
	
	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		Number av = getValue(a, ic);
		Number bv = getValue(b, ic);
		Number rv = compute(av, bv);
		return new OpResultValueHolder(rv);
	}

	private Number compute(Number av, Number bv) throws TaigaException {
		if(op == Operator.NEG) {
			if(av == null)
				return null;
			if(av instanceof Integer)
				return -av.intValue();
			else if(av instanceof Long)
				return -av.longValue();
			else if(av instanceof Float)
				return -av.floatValue();
			else
				return -av.doubleValue();
		}
		if(av == null || bv == null)
			return null;
		if(av instanceof Double || bv instanceof Double) {
			switch(op) {
			case ADD: return av.doubleValue() + bv.doubleValue();
			case SUB: return av.doubleValue() - bv.doubleValue();
			case MUL: return av.doubleValue() * bv.doubleValue();
			case DIV: return av.doubleValue() / bv.doubleValue();
			default : logAndThrow(ErrorCode.NOT_IMPLEMENTED); return null;
			}
		}
		else if(av instanceof Float || bv instanceof Float) {
			switch(op) {
			case ADD: return av.floatValue() + bv.floatValue();
			case SUB: return av.floatValue() - bv.floatValue();
			case MUL: return av.floatValue() * bv.floatValue();
			case DIV: return av.floatValue() / bv.floatValue();
			default : logAndThrow(ErrorCode.NOT_IMPLEMENTED); return null;
			}
		}
		else if(av instanceof Long || bv instanceof Long) {
			switch(op) {
			case ADD: return av.longValue() + bv.longValue();
			case SUB: return av.longValue() - bv.longValue();
			case MUL: return av.longValue() * bv.longValue();
			case DIV: return av.longValue() / bv.longValue();
			default : logAndThrow(ErrorCode.NOT_IMPLEMENTED); return null;
			}
		}
		else if(av instanceof Integer || bv instanceof Integer) {
			switch(op) {
			case ADD: return av.intValue() + bv.intValue();
			case SUB: return av.intValue() - bv.intValue();
			case MUL: return av.intValue() * bv.intValue();
			case DIV: return av.intValue() / bv.intValue();
			default : logAndThrow(ErrorCode.NOT_IMPLEMENTED); return null;
			}
		} else {
			logAndThrow(ErrorCode.NOT_SUPPORTED); return null;
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

	private class OpResultValueHolder implements ValueHolder {

		private Object opResult;
		
		
		private OpResultValueHolder(Object opResult) {
			super();
			this.opResult = opResult;
		}

		@Override
		public Property getProperty(Class<?> expectedClass) throws TaigaException {
			try {
				Property p = getModel().createNode(expectedClass);
				p.setValue(opResult);
				return p;
			} catch (ModelException e) {
				logAndThrow(e);
				return null;
			}
		}

		@Override
		public Object getValue(Class<?> expectedClass) throws TaigaException {
			Model model = getModel();
			if(opResult == null)
				return null;
			if(model.isInstance(expectedClass, opResult))
				return opResult;
			try {
				return model.convert(opResult, expectedClass);
			} catch (ModelException e) {
				logAndThrow(e);
				return null;
			}
		}

	}
	
}
