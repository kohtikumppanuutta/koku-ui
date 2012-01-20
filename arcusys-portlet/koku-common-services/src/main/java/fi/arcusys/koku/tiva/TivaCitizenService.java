package fi.arcusys.koku.tiva;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.citizenservice.ConsentShortSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentTO;
import fi.arcusys.koku.tiva.citizenservice.KokuKunpoSuostumusService_Service;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Retrieves Tiva consent data and related operations via web services
 * @author Jinhua Chen
 * Aug 11, 2011
 */
public class TivaCitizenService {
	
	private static final Logger LOG = Logger.getLogger(TivaCitizenService.class);		
	public static final URL TIVA_CITIZEN_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("TivaCitizenService WSDL location: " + KoKuPropertiesUtil.get("TivaCitizenService"));
			TIVA_CITIZEN_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("TivaCitizenService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create TivaCitizenService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	private KokuKunpoSuostumusService_Service kks;
	
	/**
	 * Constructor and initialization
	 */
	public TivaCitizenService() {
		this.kks = new KokuKunpoSuostumusService_Service(TIVA_CITIZEN_WSDL_LOCATION);
	}
	
	/**
	 * Gets assigned consents
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of summary consents
	 */
	public List<ConsentShortSummary> getAssignedConsents(String user, int startNum, int maxNum) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getAssignedConsents(user, startNum, maxNum);			
		} catch (RuntimeException re) {
			throw new KokuServiceException("getAssignedConsents failed. User: '"+user+"'", re);
		}
	}
	
	/**
	 * Gets consent in detail
	 * @param consentId consent id
	 * @return detailed consent
	 */
	public ConsentTO getConsentById(long consentId, String user) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getConsentById(consentId, user);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getConsentById failed. User: '"+user+"' consentId: '"+consentId + "'", re);
		}
	}
	
	/**
	 * Gets own consents
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of consents
	 */
	public List<ConsentSummary> getOwnConsents(String user, int startNum, int maxNum) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getOwnConsents(user, startNum, maxNum);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getOwnConsents failed. User: '"+user+"'", re);
		}
	}
	
	/**
	 * Gets own old/revoked/expired consents
	 * 
	 * @param userId
	 * @param startNum
	 * @param maxNum
	 * @return a list of consents
	 */
	public List<ConsentSummary> getOldConsents(String userId, int startNum, int maxNum) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getOldConsents(userId, startNum, maxNum);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getOldConsents failed. UserId: '"+userId+"'", re);
		}
	}
	
	
	/**
	 * Gets the total number of assigned consents
	 * @param user user name
	 * @return the total number of assigned consents
	 */
	public int getTotalAssignedConsents(String user) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getTotalAssignedConsents(user);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getTotalAssignedConsents failed. UserId: '"+user+"'", re);
		}
	}
	
	/**
	 * Gets the total number of own consents
	 * @param user user name
	 * @return the total number of own consents
	 */
	public int getTotalOwnConsents(String user) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getTotalOwnConsents(user);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getTotalOwnConsents failed. UserId: '"+user+"'", re);
		}
	}
	
	/**
	 * Gets the total number of old own consents
	 * @param user user name
	 * @return the total number of own consents
	 */
	public int getTotalOldConsents(String user) throws KokuServiceException {
		try {
			return kks.getKokuKunpoSuostumusServicePort().getTotalOldConsents(user);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getTotalOldConsents failed. UserId: '"+user+"'", re);
		}
	}
	
	/**
	 * Revokes consent
	 * @param consentId consent id
	 */
	public void revokeOwnConsent(long consentId, String user) throws KokuServiceException {
		try {
			kks.getKokuKunpoSuostumusServicePort().revokeOwnConsent(consentId, user);
		} catch (RuntimeException re) {
			throw new KokuServiceException("getTotalOldConsents failed. UserId: '"+user+"' consentId: '"+consentId+"'", re);
		}
	}
	
}
