package taiga.interfaces;

import java.util.Arrays;

public class ErrorMessage {
	public ErrorCode errorCode;
	public Position startPos;
	public String fragment;
	public Object[] parameters;
	public Exception cause;
	
	public ErrorMessage(ErrorCode errorCode, Position startPos,
			String fragment, Exception cause, Object... parameters) {
		assert startPos != null;
		this.errorCode = errorCode;
		this.startPos = startPos;
		this.fragment = fragment;
		this.cause = cause;
		this.parameters = parameters;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ErrorMessage))
			return false;
		ErrorMessage em = (ErrorMessage)obj;
		return errorCode.equals(em.errorCode) && startPos.equals(em.startPos) && Arrays.equals(parameters, em.parameters);
	}
	
	@Override
	public int hashCode() {
		return startPos.hashCode() ^ (errorCode.hashCode() * 31);
	}
	
	@Override
	public String toString() {
		return (startPos.isUndefined() ? "": (startPos.toString() + ": ")) + errorCode.format(parameters);
	}
}