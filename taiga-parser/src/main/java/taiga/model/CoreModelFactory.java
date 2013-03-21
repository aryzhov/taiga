package taiga.model;

import taiga.interfaces.Model;
import taiga.parser.ModelFactory;

public class CoreModelFactory extends AbstractModelFactory {

	public CoreModelFactory(ModelFactory next) {
		super("core", next);
	}

	@Override
	protected Model doCreateModel() {
		return new CoreModel();
	}

}
