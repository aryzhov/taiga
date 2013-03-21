package taiga.parser;

import taiga.interfaces.ErrorMessage;
import taiga.interfaces.ModelException;
import taiga.interfaces.Position;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;

class PropertyValueHolder implements ValueHolder {

	private Property property;

	public PropertyValueHolder(Property property) {
		this.property = property;
	}

	@Override
	public Property getProperty(Class<?> expectedClass) throws TaigaException {
		return property;
	}

	@Override
	public Object getValue(Class<?> expectedClass) throws TaigaException {
		try {
			return property.getValue(expectedClass);
		} catch (ModelException e) {
			throw new TaigaException(new ErrorMessage(e.getErrorCode(), Position.UNDEFINED, null, null, e.getParams()));
		}
	}

}
