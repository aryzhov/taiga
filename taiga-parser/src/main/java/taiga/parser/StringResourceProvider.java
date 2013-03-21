package taiga.parser;

import taiga.interfaces.ModelException;
import taiga.interfaces.ResourceProvider;

public class StringResourceProvider implements ResourceProvider {

	private String text;

	public StringResourceProvider(String text) {
		this.text = text;
	}

	@Override
	public String getRelativeName() {
		return "";
	}

	@Override
	public String getAbsoluteName() {
		return "";
	}

	@Override
	public String read() throws ModelException {
		return text;
	}

	@Override
	public ResourceProvider getResource(String name) {
		return null;
	}

}
