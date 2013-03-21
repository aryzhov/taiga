package taiga.parser;

import taiga.interfaces.ModelException;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;

abstract class ValueHandlerParserContext extends ParserContext implements ValueHolder {

	protected ValueHandlerParserContext(ParserContext parent) {
		super(parent);
	}

	@Override
	public ValueHolder instantiate(InstanceContext ic) throws TaigaException {
		return this;
	}

	@Override
	public abstract Object getValue(Class<?> expectedClass) throws TaigaException;

	@Override
	public Property getProperty(Class<?> expectedClass) throws TaigaException {
		Object value = getValue(expectedClass);
		if(value == ERROR)
			return null;
		try {
			Property p = getModel().createNode(expectedClass);
			if(p != null)
				p.setValue(value);
			return p;
		} catch (ModelException ex) {
			logAndThrow(ex);
			return null;
		}
	}

	@Override
	public void init() throws TaigaException {
	}
	
	@Override
	public boolean hasDependency(String typeName) {
		return false;
	}
}