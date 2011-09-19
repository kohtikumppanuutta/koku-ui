package fi.arcusys.koku.tiva.warrant.citizens;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.employee.KokuEmployeeWarrantService;
import fi.arcusys.koku.tiva.warrant.model.AuthorizationSummary;


public class KokuCitizenWarrantHandle extends AbstractHandle {

	private Logger LOG = Logger.getLogger(KokuCitizenWarrantHandle.class);

	private KokuCitizenWarrantService service;
	
	
	/**
	 * Constructor and initialization
	 */
	public KokuCitizenWarrantHandle() {
		service = new KokuCitizenWarrantService();
	}

	public List<AuthorizationSummary> getSentWarrants(String userId, int startNum, int maxNum) {
		 List<AuthorizationShortSummary> summaries = service.getSentAuthorizations(userId, startNum, maxNum);
		 List<AuthorizationSummary> summariesModels = new ArrayList<AuthorizationSummary>();
		 for (AuthorizationShortSummary summary : summaries) {
			 summariesModels.add(new AuthorizationSummary(summary));
		 }
		 return summariesModels;
	}
	
	public List<AuthorizationSummary> getReceivedAuthorizations(String userId, int startNum, int maxNum) {
		 List<AuthorizationShortSummary> summaries = service.getSentAuthorizations(userId, startNum, maxNum);
		 List<AuthorizationSummary> summariesModels = new ArrayList<AuthorizationSummary>();
		 for (AuthorizationShortSummary summary : summaries) {
			 summariesModels.add(new AuthorizationSummary(summary));
		 }
		 return summariesModels;	
	}
	
	public AuthorizationSummary getAuthorizationSummaryById(long authorizationId, String userId) {
		return new AuthorizationSummary(service.getAuthorizationSummaryById(authorizationId, userId));
	}
	
	public int getTotalSentAuthorizations(String userId) {
		return service.getTotalSentAuthorizations(userId);
	}
	
	
}
