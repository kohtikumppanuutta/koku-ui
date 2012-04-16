package fi.arcusys.koku.tiva.warrant.citizens;

import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;

import java.util.List;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.KokuKunpoValtakirjaService;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.KokuKunpoValtakirjaService_Service;
import fi.arcusys.koku.util.Properties;


public class KokuCitizenWarrantService {
		
	private static final Logger LOG = Logger.getLogger(KokuCitizenWarrantService.class);		
	
	private final KokuKunpoValtakirjaService service;
	
	public KokuCitizenWarrantService() {
		final KokuKunpoValtakirjaService_Service serviceInit = new KokuKunpoValtakirjaService_Service();
		service = serviceInit.getKokuKunpoValtakirjaServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.KOKU_KUNPO_VALTAKIRJA_SERVICE);
	}
	
	public AuthorizationSummary getAuthorizationSummaryById(long authorizationId, String userId) throws KokuServiceException {
		try {
			return service.getAuthorizationSummaryById(authorizationId, userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getAuthorizationSummaryById failed. userId: '"+userId+"' authorizationId: '"+authorizationId+"'", e);
		}
	}
	
	public List<AuthorizationShortSummary> getReceivedAuthorizations(String userId, int startNum, int maxNum) throws KokuServiceException {
		try {
			return service.getReceivedAuthorizations(userId, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getReceivedAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public List<AuthorizationShortSummary> getSentAuthorizations(String userId, int startNum, int maxNum) throws KokuServiceException {
		try {	
			return service.getSentAuthorizations(userId, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getSentAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public int getTotalSentAuthorizations(String userId) throws KokuServiceException {
		try {
			return service.getTotalSentAuthorizations(userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalSentAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public int getTotalReceivedAuthorizations(String userId) throws KokuServiceException {
		try {
			return service.getTotalReceivedAuthorizations(userId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalReceivedAuthorizations failed. userId: '"+userId+"'", e);
		}
	}
	
	public String revokeOwnAuthorization(long authorizationId, String user, String comment) {
		try {
			service.revokeOwnAuthorization(authorizationId, user, comment);
			return RESPONSE_OK;
		} catch (RuntimeException e) {
			LOG.error("Warrant revoking authorization failed. authorizationId: '"+authorizationId
					+ "' user: '"+user+"' comment: '"+comment+"'");
			return RESPONSE_FAIL;
		}
	}
}

