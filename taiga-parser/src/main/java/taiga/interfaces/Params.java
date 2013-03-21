package taiga.interfaces;

public interface Params {

	public int size();

	/** Selects the most appropriate signature among the supplied ones or throws an exception if none matches */
	public <S extends Signature> S selectSignature(S... signatures) throws ModelException; 
	
	public Object[] getParameterValues(Signature signature) throws ModelException;
	
	public <T> T getParameter(int idx, Class<T> baseClass) throws ModelException;
	
}