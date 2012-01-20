package fi.arcusys.koku.tiva.warrant.citizens;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.warrant.AbstractWarrantHandle;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationSummary;
import fi.arcusys.koku.users.KokuUserService;


public class KokuCitizenWarrantHandle extends AbstractWarrantHandle {

	private final static Logger LOG = Logger.getLogger(KokuCitizenWarrantHandle.class);

	private final KokuCitizenWarrantService service;	
	private final KokuUserService userService;	
	
	/**
	 * Constructor and initialization
	 */
	public KokuCitizenWarrantHandle() {
		service = new KokuCitizenWarrantService();
		userService = new KokuUserService();
	}

	public List<KokuAuthorizationSummary> getSentWarrants(String userId, int startNum, int maxNum) throws KokuServiceException {
		 List<AuthorizationShortSummary> summaries = service.getSentAuthorizations(userId, startNum, maxNum);
		 List<KokuAuthorizationSummary> summariesModels = new ArrayList<KokuAuthorizationSummary>();
		 for (AuthorizationShortSummary summary : summaries) {
			 KokuAuthorizationSummary shortSummary = new KokuAuthorizationSummary(summary);
			 shortSummary.setLocalizedStatus(getLocalizedAuthStatus(shortSummary.getStatus()));
			 summariesModels.add(shortSummary);
		 }
		 return summariesModels;
	}
	
	public List<KokuAuthorizationSummary> getReceivedAuthorizations(String userId, int startNum, int maxNum) throws KokuServiceException {
		 List<AuthorizationShortSummary> summaries = service.getReceivedAuthorizations(userId, startNum, maxNum);
		 List<KokuAuthorizationSummary> summariesModels = new ArrayList<KokuAuthorizationSummary>();
		 for (AuthorizationShortSummary summary : summaries) {
			 KokuAuthorizationSummary shortSummary = new KokuAuthorizationSummary(summary);
			 shortSummary.setLocalizedStatus(getLocalizedAuthStatus(shortSummary.getStatus()));
			 summariesModels.add(shortSummary);
		 }
		 return summariesModels;	
	}
	
	public KokuAuthorizationSummary getAuthorizationSummaryById(long authorizationId, String userId) throws KokuServiceException {
		
		KokuAuthorizationSummary summary = new KokuAuthorizationSummary(service.getAuthorizationSummaryById(authorizationId, userId));
		if (summary.getStatus() != null) {
			summary.setLocalizedStatus(getLocalizedAuthStatus(summary.getStatus()));
		}
		if (summary.getType() != null) {
			summary.setLocalizedType(getLocalizedWarrantCreateType(summary.getType()));
		}
		if (summary.getSenderUid() != null && summary.getReceiverUid() != null) {
			summary.setSenderName(userService.getKunpoNameByUserUid(summary.getSenderUid()));
			summary.setRecieverName(userService.getKunpoNameByUserUid(summary.getReceiverUid()));
			summary.setTargetPersonName(userService.getKunpoNameByUserUid(summary.getTargetPersonUid()));
		}
		return summary;
	}
	
	public int getTotalSentAuthorizations(String userId) throws KokuServiceException {
		return service.getTotalSentAuthorizations(userId);
	}
	
	public int getTotalReceivedAuthorizations(String userId) throws KokuServiceException {
		return service.getTotalReceivedAuthorizations(userId);
	}
	
	public String revokeOwnAuthorization(long authorizationId, String user, String comment) {
		return service.revokeOwnAuthorization(authorizationId, user, comment);
	}	
	
	
}
