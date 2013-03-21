package taiga.html;

import taiga.interfaces.Model;
import taiga.model.AbstractModelFactory;
import taiga.parser.ModelFactory;

public class HTMLModelFactory extends AbstractModelFactory {

	public HTMLModelFactory(ModelFactory next) {
		super("html", next);
	}

	@Override
	protected Model doCreateModel() {
		return new HTMLModel();
	}

}
