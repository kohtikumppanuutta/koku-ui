package fi.arcusys.koku.web.util.exception;

import java.util.UUID;

/**
 * Signals that an some sort error has occurred while trying to do Koku process. 
 * This class is the general class of exceptions produced by failed Koku processes.
 *
 * @author Toni Turunen
 */
public class KokuProcessException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public static String generateErrorCode() {
		return UUID.randomUUID().toString();
	}
	
	private final String uuid;
	
	public KokuProcessException() {
		super();
		uuid = generateErrorCode();
	}

	public KokuProcessException(String message) {
		super(message);
		uuid = generateErrorCode();
	}

	public KokuProcessException(String message, Throwable cause) {
		super(message, cause);
		uuid = generateErrorCode();
	}
	
	private final String getErrorCode() {
		return "Unique KOKU error code: '" + uuid + "'. ";
	}

	@Override
	public String getMessage() {
		return getErrorCode() + super.getMessage();
	}

	public final String getUuid() {
		return uuid;
	}		
	
}
