package fi.arcusys.koku.palvelut.exceptions;

/**
 * The base exception for unexpected processing errors. This Exception
 * class is used to report well-formedness errors as well as unexpected
 * processing conditions.
 */
public class IllegalOperationCall extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalOperationCall() {
		super();
	}

	public IllegalOperationCall(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalOperationCall(String message) {
		super(message);
	}

	public IllegalOperationCall(Throwable cause) {
		super(cause);
	}
	

}
