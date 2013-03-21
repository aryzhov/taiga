package taiga.interfaces;


public class ModelException extends Exception {
	private ErrorCode errorCode;
	private Object[] params;

	public ModelException(Exception cause, ErrorCode errorCode, Object...params) {
		super(cause);
		assert errorCode != null;
		this.errorCode = errorCode;
		this.params = params;
	}

	public ModelException(ErrorCode errorCode, Object...params) {
		this(null, errorCode, params);
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	public Object[] getParams() {
		return params;
	}
}
