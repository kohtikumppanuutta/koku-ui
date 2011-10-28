package fi.arcusys.koku.palvelut.proxy;

/**
 * Exception class used for error situations in the proxy filter.
 * 
 * @author Jon Haikarainen
 */
public class ProxyFilterException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProxyFilterException(String message) {
        super(message);
    }

    public ProxyFilterException(String message, Exception cause) {
        super(message, cause);
    }

    public ProxyFilterException(Exception cause) {
        super(cause);
    }

}
