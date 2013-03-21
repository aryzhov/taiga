package taiga.beans;

import taiga.interfaces.Model;
import taiga.model.AbstractModelFactory;
import taiga.parser.ModelFactory;

public class BeansModelFactory extends AbstractModelFactory {

	public BeansModelFactory(ModelFactory next) {
		super("bean", next);
	}

	@Override
	protected Model doCreateModel() {
		return new BeanModel();
	}

}
