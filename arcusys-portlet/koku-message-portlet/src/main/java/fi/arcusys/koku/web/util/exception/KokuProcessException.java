package fi.arcusys.koku.web.util.exception;

/**
 * Signals that an some sort error has occurred while trying to do Koku process. 
 * This class is the general class of exceptions produced by failed Koku processes.
 *
 * @author Toni Turunen
 */
public class KokuProcessException extends Exception {

	private static final long serialVersionUID = 1L;

	public KokuProcessException() {
		super();
	}

	public KokuProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public KokuProcessException(String message) {
		super(message);
	}

	public KokuProcessException(Throwable cause) {
		super(cause);
	}
	
}
