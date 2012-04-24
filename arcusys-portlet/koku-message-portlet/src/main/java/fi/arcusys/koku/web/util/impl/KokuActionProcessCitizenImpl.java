package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;

import java.util.Arrays;

import fi.arcusys.koku.av.AvCitizenServiceHandle;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.TivaCitizenServiceHandle;
import fi.arcusys.koku.tiva.warrant.citizens.KokuCitizenWarrantHandle;
import fi.arcusys.koku.web.util.exception.KokuActionProcessException;

public class KokuActionProcessCitizenImpl extends AbstractKokuActionProcess {
		
	/* Lazily initialized */
	private AvCitizenServiceHandle avCitizenServiceHandle = null;
	private KokuCitizenWarrantHandle warrantHandle = null;	
	private TivaCitizenServiceHandle tivaHandle = null;

	public KokuActionProcessCitizenImpl(String userId) {
		super(userId);
	}

	@Override
	public void cancelAppointments(String[] appointmentIds, String[] targetPersons, String comment) throws KokuActionProcessException {
		
		if (appointmentIds == null || targetPersons == null) {
			throw new KokuActionProcessException("AppoimentsId or targetPersons parameter(s) are null");
		}
		
		/* Lazy services loading */
		if (avCitizenServiceHandle == null) {
			avCitizenServiceHandle = new AvCitizenServiceHandle(getUserId());
		}

		final int appointments = appointmentIds.length;
		final int targetPersonCount = targetPersons.length;
		
		if (appointments != targetPersonCount) {
			throw new KokuActionProcessException("Appointments and targetPerson count doesn't match! appointments: '"+
					Arrays.toString(appointmentIds)+"' targetPersons: '"+Arrays.toString(targetPersons)+"'");
		}
		
		String appointmentId;
		String targetPerson;
		for (int i=0; i < appointments; i++ ) {
			appointmentId = appointmentIds[i];
			targetPerson = targetPersons[i];
			long  appId = 0;
			try {
				appId = (long) Long.parseLong(appointmentId);
				avCitizenServiceHandle.cancelAppointments(appId, targetPerson, comment);
			} catch (NumberFormatException nfe) {
				throw new KokuActionProcessException("Invalid appointmentId. AppointmentId: '"+appointmentId+"' targetPerson: '"+targetPerson+"' comment: '"+comment+"'", nfe);
			} catch (KokuServiceException e) {
				throw new KokuActionProcessException("Failed to cancelAppointment! appoimentId: '" + 
						appointmentId + "' targetPerson: '" + targetPerson + "' comment: '" + comment + "'", e);
			}
		}
	}

	@Override
	public void revokeWarrants(String[] warrantIds, String comment)
			throws KokuActionProcessException {

		if (warrantIds == null) {
			throw new KokuActionProcessException("warrantIds parameter is null");
		}
		
		/* Lazy initialization */
		if (warrantHandle == null) {
			warrantHandle = new KokuCitizenWarrantHandle();				
		}
		
		for(String authorizationId : warrantIds) {
			try {
				long authId = Long.parseLong(authorizationId);
				String revokeResult = warrantHandle.revokeOwnAuthorization(authId, getUserId(), comment);
				if (revokeResult.equals(RESPONSE_FAIL)) {
					throw new KokuActionProcessException("Revoking warrant failed! authId: '"+authId+"' userId: '"+getUserId()+"' comment:'"+comment+"'");
				}
			} catch (NumberFormatException nfe) {
				throw new KokuActionProcessException("Couldn't revoke warrant! Invalid authorizationId. UserId: "+ getUserId() + " AuthorizationId: "+ authorizationId + "Comment: " + comment, nfe);
			}
		}		
	}

	@Override
	public void revokeConsents(String[] consentIds)
			throws KokuActionProcessException {

		if (consentIds == null) {
			throw new KokuActionProcessException("consentIds parameter is null");
		}
		if (tivaHandle == null) {
			tivaHandle = new TivaCitizenServiceHandle(getUserId());			
		}
		try {
			for(String consentId : consentIds) {
				String revokingResult;
					revokingResult = tivaHandle.revokeOwnConsent(consentId);
				if (revokingResult.equals(RESPONSE_FAIL)) {
					throw new KokuActionProcessException("Failed to revoke consent. consentId: '"+consentId+"'");
				}
			}
		} catch (KokuServiceException e) {
			throw new KokuActionProcessException("Failed to revoke consent(s). ConsentIds: '"+Arrays.toString(consentIds)+"'", e);
		}
	}	
}
