package taiga.gwt.model;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Params;

public abstract class InterfaceDesc<W> extends ClassDesc<W> {

	@Override
	public String getTypeName() {
		return null;
	}

	@Override
	public Class<?> getWidgetClass() {
		return null;
	}
	
	@Override
	public W newInstance(Params params) throws ModelException {
		throw new ModelException(ErrorCode.CANNOT_INSTANTIATE_ABSTRACT_CLASS, getTypeName());
	}

}
