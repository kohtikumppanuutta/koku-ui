package fi.arcusys.koku.tiva;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.tiva.citizenservice.ConsentShortSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentTO;
import fi.arcusys.koku.tiva.citizenservice.KokuKunpoSuostumusService_Service;

/**
 * 
 * @author Jinhua Chen
 * Aug 11, 2011
 */
public class TivaCitizenService {
	public final URL TIVA_CITIZEN_WSDL_LOCATION = getClass().getClassLoader().getResource("TivaCitizenService.wsdl");
	private KokuKunpoSuostumusService_Service kks;
	
	public TivaCitizenService() {
		this.kks = new KokuKunpoSuostumusService_Service(TIVA_CITIZEN_WSDL_LOCATION);
	}
	
	public List<ConsentShortSummary> getAssignedConsents(String user, int startNum, int maxNum) {
		return kks.getKokuKunpoSuostumusServicePort().getAssignedConsents(user, startNum, maxNum);
	}
	
	public ConsentTO getConsentById(long consentId) {
		return kks.getKokuKunpoSuostumusServicePort().getConsentById(consentId);
	}
	
	public List<ConsentSummary> getOwnConsents(String user, int startNum, int maxNum) {
		return kks.getKokuKunpoSuostumusServicePort().getOwnConsents(user, startNum, maxNum);
	}
	
	public int getTatalAssignedConsents(String user) {
		return kks.getKokuKunpoSuostumusServicePort().getTotalAssignedConsents(user);
	}
	
	public int getTotalOwnConsents(String user) {
		return kks.getKokuKunpoSuostumusServicePort().getTotalOwnConsents(user);
	}
	
	public void revokeOwnConsent(long consentId) {
		kks.getKokuKunpoSuostumusServicePort().revokeOwnConsent(consentId);
	}
	
}
