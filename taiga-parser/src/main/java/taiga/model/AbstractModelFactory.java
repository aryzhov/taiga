package taiga.model;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.Model;
import taiga.interfaces.ModelException;
import taiga.interfaces.StringUtils;
import taiga.parser.ModelFactory;

public abstract class AbstractModelFactory implements ModelFactory {
	private ModelFactory next;
	private String modelName;

	public AbstractModelFactory(String modelName, ModelFactory next) {
		this.modelName = modelName;
		this.next = next;
		
	}
	
	@Override
	public Model getDefaultModel() throws ModelException {
		return doCreateModel();
	}
	
	@Override
	public Model createModel(String modelName) throws ModelException {
		if(StringUtils.equals(this.modelName, modelName, false))
			return doCreateModel();
		else if(next != null)
			return next.createModel(modelName);
		else
			throw new ModelException(ErrorCode.MODEL_CLASS_NOT_FOUND, modelName);
	}

	protected abstract Model doCreateModel();
	
}
