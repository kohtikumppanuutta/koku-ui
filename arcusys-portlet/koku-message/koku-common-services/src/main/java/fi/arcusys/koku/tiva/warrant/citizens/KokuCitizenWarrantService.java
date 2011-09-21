package fi.arcusys.koku.tiva.warrant.citizens;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.KokuKunpoValtakirjaService_Service;


public class KokuCitizenWarrantService {
	
	public final URL WARRANT_SERVICE_WSDL_LOCATION = getClass().getClassLoader().getResource("KokuKunpoValtakirjaServiceImpl.wsdl");
	private KokuKunpoValtakirjaService_Service service;
	
	
	public KokuCitizenWarrantService() {
		service = new KokuKunpoValtakirjaService_Service(WARRANT_SERVICE_WSDL_LOCATION);
	}
	
	public AuthorizationSummary getAuthorizationSummaryById(long authorizationId, String userId) {
		return service.getKokuKunpoValtakirjaServicePort().getAuthorizationSummaryById(authorizationId, userId);
	}
	
	public List<AuthorizationShortSummary> getReceivedAuthorizations(String userId, int startNum, int maxNum) {
		return service.getKokuKunpoValtakirjaServicePort().getReceivedAuthorizations(userId, startNum, maxNum);
	}
	
	public List<AuthorizationShortSummary> getSentAuthorizations(String userId, int startNum, int maxNum) {
		return service.getKokuKunpoValtakirjaServicePort().getSentAuthorizations(userId, startNum, maxNum);
	}
	
	public int getTotalSentAuthorizations(String userId) {
		return service.getKokuKunpoValtakirjaServicePort().getTotalSentAuthorizations(userId);
	}
	
	public int getTotalReceivedAuthorizations(String userId) {
		return service.getKokuKunpoValtakirjaServicePort().getTotalReceivedAuthorizations(userId);
	}
	
	public void revokeOwnAuthorization(long authorizationId, String user, String comment) {
		service.getKokuKunpoValtakirjaServicePort().revokeOwnAuthorization(authorizationId, user, comment);
	}
}

