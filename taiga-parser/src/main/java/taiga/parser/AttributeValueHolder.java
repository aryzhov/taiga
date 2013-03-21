package taiga.parser;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ErrorMessage;
import taiga.interfaces.ModelException;
import taiga.interfaces.Node;
import taiga.interfaces.Params;
import taiga.interfaces.Position;
import taiga.interfaces.Property;
import taiga.interfaces.TaigaException;

public class AttributeValueHolder implements ValueHolder {

	private String attrName;
	private Object value;
	
	public AttributeValueHolder(String attrName, Object value) {
		this.attrName = attrName;
		this.value = value;
	}

	@Override
	public Object getValue(Class<?> expectedClass) throws TaigaException {
// TODO: implement in GWT-safe way
//		if(expectedClass.isInstance(value))
			return value;
//		else
//			throw new TaigaException(new ErrorMessage(ErrorCode.INCOMPATIBLE_ATTR_TYPE, Position.UNDEFINED, null, null, attrName, expectedClass)); 
	}

	
	@Override
	public Property getProperty(Class<?> expectedClass) throws TaigaException {
		getValue(expectedClass);
		return new SimpleProperty();
	}

	private class SimpleProperty implements Property {

		@Override
		public Object getName() {
			return null;
		}

		@Override
		public Node getParent() {
			return null;
		}

		@Override
		public Class<?> getValueClass() {
			return value == null ? null : value.getClass();
		}

		@Override
		public void init(Params params, Iterable<Node> scope) throws ModelException {
			throw new ModelException(ErrorCode.NOT_SUPPORTED);
		}

		@Override
		public boolean assignTo(Node node, Object propName) throws ModelException {
			node.setPropertyValue(propName, value);
			return false;
		}

		@Override
		public void setValue(Object value) throws ModelException {
			throw new ModelException(ErrorCode.NOT_SUPPORTED);
		}

		@Override
		public boolean isValueSet() {
			return true;
		}

		@Override
		public Object getValue() throws ModelException {
			return value;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getValue(Class<T> cl) throws ModelException {
			return (T)value;
		}

	}


	
}
