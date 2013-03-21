package taiga.parser;

import taiga.interfaces.ErrorCode;
import taiga.interfaces.ModelException;
import taiga.interfaces.Params;
import taiga.interfaces.Signature;

public class EmptyParams implements Params {

	@Override
	public int size() {
		return 0;
	}

	public <T> T getParameter(int idx, Class<T> baseClass) throws ModelException {
		throw new ModelException(ErrorCode.INVALID_PARAM_INDEX, idx);
	}

	@Override
	public <S extends Signature> S selectSignature(S... signatures) throws ModelException { 
		for(S s: signatures)
			if(s.isEmpty())
				return s;
		throw new ModelException(ErrorCode.PARAM_SIGNATURE_MISMATCH);
	}

	@Override
	public Object[] getParameterValues(Signature signature) throws ModelException {
		if(signature.isEmpty())
			return new Object[0];
		else
			throw new ModelException(ErrorCode.PARAM_SIGNATURE_MISMATCH);
	}

}
