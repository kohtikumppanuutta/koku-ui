package fi.arcusys.koku.tiva.warrant.citizens;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
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
	
	public AuthorizationSummary getAuthorizationSummaryById(long authorizationId, String userId) throws KokuServiceException {
		try {
			return service.getKokuKunpoValtakirjaServicePort().getAuthorizationSummaryById(authorizationId, userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAuthorizationSummaryById failed. userId: '"+userId+"' authorizationId: '"+authorizationId+"'", e);
		}
	}
	
	public List<AuthorizationShortSummary> getReceivedAuthorizations(String userId, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getKokuKunpoValtakirjaServicePort().getReceivedAuthorizations(userId, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getReceivedAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public List<AuthorizationShortSummary> getSentAuthorizations(String userId, int startNum, int maxNum) throws KokuServiceException {
		try {	
			return service.getKokuKunpoValtakirjaServicePort().getSentAuthorizations(userId, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getSentAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public int getTotalSentAuthorizations(String userId) throws KokuServiceException {
		try {
			return service.getKokuKunpoValtakirjaServicePort().getTotalSentAuthorizations(userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalSentAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public int getTotalReceivedAuthorizations(String userId) throws KokuServiceException {
		try {
			return service.getKokuKunpoValtakirjaServicePort().getTotalReceivedAuthorizations(userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalReceivedAuthorizations failed. userId: '"+userId+"'", e);
		}
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

