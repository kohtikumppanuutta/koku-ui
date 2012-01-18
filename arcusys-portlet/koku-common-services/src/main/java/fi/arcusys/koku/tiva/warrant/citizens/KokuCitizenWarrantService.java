package fi.arcusys.koku.tiva.warrant.citizens;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.tiva.TivaEmployeeService;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.KokuKunpoValtakirjaService_Service;
import static fi.arcusys.koku.util.Constants.*;
import fi.koku.settings.KoKuPropertiesUtil;


public class KokuCitizenWarrantService {
		
	private static final Logger LOG = Logger.getLogger(KokuCitizenWarrantService.class);		
	public static final URL WARRANT_SERVICE_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("KokuKunpoValtakirjaService WSDL location: " + KoKuPropertiesUtil.get("KokuKunpoValtakirjaService"));
			WARRANT_SERVICE_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("KokuKunpoValtakirjaService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create KokuKunpoValtakirjaService WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	
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
	
	public String revokeOwnAuthorization(long authorizationId, String user, String comment) {
		try {
			service.getKokuKunpoValtakirjaServicePort().revokeOwnAuthorization(authorizationId, user, comment);
			return RESPONSE_OK;
		} catch (RuntimeException e) {
			LOG.error("Warrant revoking authorization failed. authorizationId: '"+authorizationId
					+ "' user: '"+user+"' comment: '"+comment+"'");
			return RESPONSE_FAIL;
		}
	}
}

