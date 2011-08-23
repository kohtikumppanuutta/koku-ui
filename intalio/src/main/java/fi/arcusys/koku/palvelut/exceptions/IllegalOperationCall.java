package fi.arcusys.koku.palvelut.exceptions;

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
