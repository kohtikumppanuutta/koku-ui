package fi.arcusys.koku.tiva;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

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
	public List<ConsentShortSummary> getAssignedConsents(String user, int startNum, int maxNum) {
		return kks.getKokuKunpoSuostumusServicePort().getAssignedConsents(user, startNum, maxNum);
	}
	
	/**
	 * Gets consent in detail
	 * @param consentId consent id
	 * @return detailed consent
	 */
	public ConsentTO getConsentById(long consentId, String user) {
		return kks.getKokuKunpoSuostumusServicePort().getConsentById(consentId, user);
	}
	
	/**
	 * Gets own consents
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of consents
	 */
	public List<ConsentSummary> getOwnConsents(String user, int startNum, int maxNum) {
		return kks.getKokuKunpoSuostumusServicePort().getOwnConsents(user, startNum, maxNum);
	}
	
	/**
	 * Gets own old/revoked/expired consents
	 * 
	 * @param userId
	 * @param startNum
	 * @param maxNum
	 * @return a list of consents
	 */
	public List<ConsentSummary> getOldConsents(String userId, int startNum, int maxNum) {
		return kks.getKokuKunpoSuostumusServicePort().getOldConsents(userId, startNum, maxNum);
	}
	
	
	/**
	 * Gets the total number of assigned consents
	 * @param user user name
	 * @return the total number of assigned consents
	 */
	public int getTotalAssignedConsents(String user) {
		return kks.getKokuKunpoSuostumusServicePort().getTotalAssignedConsents(user);
	}
	
	/**
	 * Gets the total number of own consents
	 * @param user user name
	 * @return the total number of own consents
	 */
	public int getTotalOwnConsents(String user) {
		return kks.getKokuKunpoSuostumusServicePort().getTotalOwnConsents(user);
	}
	
	/**
	 * Gets the total number of old own consents
	 * @param user user name
	 * @return the total number of own consents
	 */
	public int getTotalOldConsents(String user) {
		return kks.getKokuKunpoSuostumusServicePort().getTotalOldConsents(user);
	}
	
	/**
	 * Revokes consent
	 * @param consentId consent id
	 */
	public void revokeOwnConsent(long consentId, String user) {
		kks.getKokuKunpoSuostumusServicePort().revokeOwnConsent(consentId, user);
	}
	
	
	
	
}
