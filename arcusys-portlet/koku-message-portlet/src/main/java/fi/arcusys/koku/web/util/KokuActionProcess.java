package fi.arcusys.koku.web.util;

import fi.arcusys.koku.web.util.exception.KokuActionProcessException;

/**
 * KOKU related processes
 * 
 * @author Toni Turunen
 *
 */
public interface KokuActionProcess {
	
	/**
	 * Cancel appointments by appointmentId (tapaaminen)
	 * 
	 * @param appointmentIds
	 * @param targetPersons
	 * @param comment
	 * @throws KokuActionProcessException
	 */
	void cancelAppointments(String[] appointmentIds, String[] targetPersons, String comment) throws KokuActionProcessException;
	/**
	 * Revoke warrants (authorizations/valtakirja)
	 * 
	 * @param warrantIds
	 * @param comment
	 * @throws KokuActionProcessException
	 */
	void revokeWarrants(String[] warrantIds, String comment) throws KokuActionProcessException;
	/**
	 * Revoke consents (pyyntö)
	 * 
	 * @param consentIds
	 * @throws KokuActionProcessException
	 */
	void revokeConsents(String[] consentIds) throws KokuActionProcessException;
	/**
	 * Delete KOKU messages
	 * 
	 * @param messageIds
	 * @throws KokuActionProcessException
	 */
	void deleteMessages(String[] messageIds) throws KokuActionProcessException;
	/**
	 * Archive KOKU messages
	 * 
	 * @param messageIds
	 * @throws KokuActionProcessException
	 */
	void archiveMessages(String[] messageIds) throws KokuActionProcessException;
	
	/**
	 * Archive KOKU messages which are older than 3(?) months. 
	 * 
	 * @param folderType
	 * @throws KokuActionProcessException
	 */
	void archiveOldMessages(String folderType) throws KokuActionProcessException;
	
}
