package fi.arcusys.koku.exceptions;

public class IntalioAuthException extends Exception {

	private static final long serialVersionUID = 1L;

	public IntalioAuthException() {
		super();
	}

	public IntalioAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public IntalioAuthException(String message) {
		super(message);
	}

	public IntalioAuthException(Throwable cause) {
		super(cause);
	}
}
