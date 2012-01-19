package fi.arcusys.koku.web.util;

import fi.arcusys.koku.web.util.exception.KokuActionProcessException;

public interface KokuActionProcess {
	
	void cancelAppointments(String[] appointmentIds, String[] targetPersons, String comment) throws KokuActionProcessException;
	void revokeWarrants(String[] warrantIds, String comment) throws KokuActionProcessException;
	void revokeConsents(String[] consentIds) throws KokuActionProcessException;
	void deleteMessages(String[] messageIds) throws KokuActionProcessException;
	void archiveMessages(String[] messageIds) throws KokuActionProcessException;
	void archiveOldMessages(String folderType) throws KokuActionProcessException;
	
}
