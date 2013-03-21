package taiga.parser;

import taiga.interfaces.Model;
import taiga.interfaces.ModelException;

public interface ModelFactory {
	public Model getDefaultModel() throws ModelException;
	public Model createModel(String modelName) throws ModelException;
}
