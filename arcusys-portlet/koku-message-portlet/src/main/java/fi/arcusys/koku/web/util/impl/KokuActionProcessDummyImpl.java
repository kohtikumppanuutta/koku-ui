package fi.arcusys.koku.web.util.impl;

import fi.arcusys.koku.web.util.exception.KokuActionProcessException;

public class KokuActionProcessDummyImpl extends AbstractKokuActionProcess {
	
	public KokuActionProcessDummyImpl(String userId) {
		super(userId);
	}

	@Override
	public void cancelAppointments(String[] appointmentIds,
			String[] targetPersons, String comment)
			throws KokuActionProcessException {
		throw new KokuActionProcessException();
	}

	@Override
	public void revokeWarrants(String[] warrantIds, String comment)
			throws KokuActionProcessException {
		throw new KokuActionProcessException();		
	}

	@Override
	public void revokeConsents(String[] consentIds)
			throws KokuActionProcessException {
		throw new KokuActionProcessException();		
	}

	@Override
	public void deleteMessages(String[] messageIds)
			throws KokuActionProcessException {
		throw new KokuActionProcessException();		
	}

	@Override
	public void archiveMessages(String[] messageIds)
			throws KokuActionProcessException {
		throw new KokuActionProcessException();		
	}

	@Override
	public void archiveOldMessages(String folderType)
			throws KokuActionProcessException {
		throw new KokuActionProcessException();		
	}
}
