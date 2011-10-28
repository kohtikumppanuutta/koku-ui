package fi.arcusys.koku.palvelut.util;

/**
 * This interface is meant to validate KOKU
 * SOAP/XML-message methods/input (e.g checking permission
 * if given message has permission to execute given methods.
 * 
 * @author Toni Turunen
 *
 */
public interface OperationsValidator {
	
	/**
	 * Returns true if content and/or methods is valid/
	 * 
	 * @param xmlData XML-data
	 * @return true if content is valid otherwise false
	 */
	boolean isValid(String xmlData);

}
