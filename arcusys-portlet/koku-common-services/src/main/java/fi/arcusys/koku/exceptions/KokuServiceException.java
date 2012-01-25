package fi.arcusys.koku.exceptions;

import java.util.UUID;


/**
 * KokuServiceException 
 * 
 * @author Toni Turunen
 */
public class KokuServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public static String generateErrorCode() {
		return UUID.randomUUID().toString();
	}
	
	private final String uuid;
	
	public KokuServiceException(String message) {
		super(message);
		uuid = generateErrorCode();
	}

	public KokuServiceException(String message, Throwable cause) {
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
	
	public final String getErrorcode() {
		return uuid;
	}	
}
