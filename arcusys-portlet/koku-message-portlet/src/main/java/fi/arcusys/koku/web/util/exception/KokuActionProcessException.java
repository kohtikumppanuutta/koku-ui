package fi.arcusys.koku.web.util.exception;

/**
 * Signals that an some sort error has occurred while trying to do ActionProcess. 
 * This class is the general class of exceptions produced by failed action processes.
 * 
 * @author Toni Turunen
 */
public class KokuActionProcessException extends KokuProcessException {

	private static final long serialVersionUID = 1L;

	public KokuActionProcessException() {
		super();
	}

	public KokuActionProcessException(String message, Throwable cause) {
		super(message, cause);
	}

	public KokuActionProcessException(String message) {
		super(message);
	}

	public KokuActionProcessException(Throwable cause) {
		super(cause);
	}
	
}
