package taiga.parser;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;
import taiga.parser.node.AArrayBlock;
import taiga.parser.node.AObjectBlock;
import taiga.parser.node.ASelect;
import taiga.parser.node.PBlock;

public class SelectValueParserContext extends TypeScopeParserContext {

	private ASelect select;
	private ParserContext value;
	private ParserContext block;

	protected SelectValueParserContext(ParserContext parent, ASelect select) throws TaigaException {
		super(parent);
		this.select = select;
		value = createValueParserContext(select.getValue());
		PBlock selector = select.getSelector(); 
		// TODO: provide new implementation classes to support expressions and references as selector values
		if(selector instanceof AArrayBlock) {
			block = new ArrayBlockParserContext(this, (AArrayBlock)selector);
		} else if(selector instanceof AObjectBlock) {
			block = new ObjectBlockParserContext(this, (AObjectBlock) selector, getTypeScope());
		} else {
			logAndThrow(ErrorCode.NOT_SUPPORTED);
		}
	}
	
	@Override
	public void init() throws TaigaException {
		super.init();
		value.init();
		block.init();
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		ValueHolder valueHolder = value.instantiate(ic);
		if(valueHolder == null)
			return null;
		Object value = valueHolder.getValue(Object.class);
		if(value == ValueHolder.ERROR)
			return null;
		ValueHolder blockValue = block.instantiate(ic);
		if(blockValue == null)
			return null;
		Property prop = blockValue.getProperty(Object.class);
		if(prop == null)
			return null;
		if(!(prop instanceof Node)) {
			logAndThrow(ErrorCode.SELECTOR_NOT_OBJECT);
			return null;
		}
		Node node = (Node) prop;
		if(!node.isPropertyValueSet(value))
			return null;
		else {
			try {
				Property p2 = node.getProperty(value);
				return new PropertyValueHolder(p2);
			} catch (ModelException e) {
				logAndThrow(e);
				return null;
			} 
		}
					
	}
	
	@Override
	protected taiga.parser.node.Node getASTNode() {
		return select;
	}

}
