package taiga.gwt.model;

import taiga.interfaces.Model;
import taiga.model.AbstractModelFactory;
import taiga.parser.ModelFactory;

public class GWTModelFactory extends AbstractModelFactory {

	public GWTModelFactory(ModelFactory next) {
		super("gwt", next);
	}

	@Override
	protected Model doCreateModel() {
		return new WidgetModel();
	}

}
