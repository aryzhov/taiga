package taiga.interfaces;

/** This exception is thrown by the parser as a result of one or more errors that occur during parsing, including syntax errors and model errors */
public class TaigaException extends Exception {
	/** The first error message that triggered the exception */
	public ErrorMessage errorMessage;

	public TaigaException(ErrorMessage errorMessage) {
		super(errorMessage.toString(), errorMessage.cause);
		this.errorMessage = errorMessage;
	}
}
