package fi.arcusys.koku.tiva.warrant.employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.warrant.AbstractWarrantHandle;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationCriteria;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationQuery;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationSummary;
import fi.arcusys.koku.tiva.warrant.model.KokuValtakirjapohja;

/**
 * Handles warrants (valtakirja) related operations for employee
 * 
 * @author Toni Turunen
 *
 */
public class KokuEmployeeWarrantHandle extends AbstractWarrantHandle {

	private final KokuEmployeeWarrantService service;
	
	private static final String SENDER_UID 			= "senderUid";
	private static final String RECIEVER_UID 		= "recieverUid";
	private static final String TARGET_PERSON_UID 	= "targetPersonUid";
	private static final String TEMPLATE_ID 		= "templateId";
	private static final String TEMPLATE_NAME		= "templateName";
	private static final String FILTER				= "filter";
	private static final int MAX_SEARCH_RESULTS 	= 1;


	public KokuEmployeeWarrantHandle() {
		service = new KokuEmployeeWarrantService();	
	}
	
	/**
	 * Return authorizations by given authorization template, recipient and user 
	 * 
	 * @param authorizationTemplateId
	 * @param receipientUid
	 * @param senderUid
	 * @param startNum
	 * @param maxNum
	 * @return list of authorizations
	 * @throws KokuServiceException
	 */
	public List<KokuAuthorizationSummary> getAuthorizations(long authorizationTemplateId, String receipientUid, String senderUid, int startNum, int maxNum) throws KokuServiceException {
		AuthorizationQuery query = new AuthorizationQuery();
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		criteria.setAuthorizationTemplateId(authorizationTemplateId);
		criteria.setReceipientUid(receipientUid);
		criteria.setSenderUid(senderUid);
		query.setCriteria(criteria);
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);
		List<AuthorizationShortSummary> authShortSummaries = service.getAuthorizations(query);
		List<KokuAuthorizationSummary> summaries = new ArrayList<KokuAuthorizationSummary>();
		for (AuthorizationShortSummary shortSummary : authShortSummaries) {
			summaries.add(new KokuAuthorizationSummary(shortSummary));
		}
		return summaries;
	}
	
	/**
	 * Search Valtakirjapohja(s) by templateName. Keyword can also
	 * be partial name of Valtakirja. 
	 * 
	 * 
	 * @param keyword templatename (also partial name accepted)
	 * @param maxNum how many results will be returned
	 * @return List of KokuValtakirjapohjas
	 */
	public List<KokuValtakirjapohja> searchWarrantTemplates(String keyword, int maxNum) throws KokuServiceException {
		List<Valtakirjapohja> warrantTemplates = service.searchAuthorizationTemplates(keyword, maxNum);
		List<KokuValtakirjapohja> kokuWarrantTemplates = new ArrayList<KokuValtakirjapohja>();
		for (Valtakirjapohja warrantTemplate : warrantTemplates) {
			kokuWarrantTemplates.add(new KokuValtakirjapohja(warrantTemplate));
		}
		return kokuWarrantTemplates;		
	}
	
	/**
	 * Returns list of recieved authorizations by given userId.
	 * 
	 * @param recipientUserId
	 * @param startNum
	 * @param maxNum
	 * @return List of Authorizations
	 */
	public List<KokuAuthorizationSummary> getAuthorizationsByUserId(String keyword, int startNum, int maxNum) throws KokuServiceException {
		AuthorizationQuery query = new AuthorizationQuery();
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		Map<String, String> searchMap = createSearchCitizenSearchMap(keyword);
		if (searchMap == null) {
			return new ArrayList<KokuAuthorizationSummary>();
		}
		criteria.setSenderUid(searchMap.get(SENDER_UID));
		criteria.setReceipientUid(searchMap.get(RECIEVER_UID));
		criteria.setTargetPersonUid(searchMap.get(TARGET_PERSON_UID));
		try {
			criteria.setAuthorizationTemplateId(Long.valueOf(searchMap.get(TEMPLATE_ID)));
		} catch (NumberFormatException nfe) {
			// Catch silently. Leave templateId criteria empty 
		}
		
		query.setCriteria(criteria);
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);
		List<AuthorizationShortSummary> authShortSummaries = service.getAuthorizations(query);
		List<KokuAuthorizationSummary> summaries = new ArrayList<KokuAuthorizationSummary>();
		for (AuthorizationShortSummary shortSummary : authShortSummaries) {
			KokuAuthorizationSummary kokuSummary = new KokuAuthorizationSummary(shortSummary);
			localize(kokuSummary);
			summaries.add(kokuSummary);
		}
		return summaries;
	}
	
	private Map<String, String> createSearchCitizenSearchMap(String keyword) {
		if (keyword == null) {
				return null;
		}
		
		Map<String, String> searchMap = new HashMap<String, String>(5);
		String[] split = keyword.split(SPLIT_REGEX);
		if (split.length == 0) {
			return null;
		}
		// FIXME: Not like this..  
		for (int i = 0; i < split.length; i++) {
			if (i == 0) {
				searchMap.put(RECIEVER_UID, split[i]);				
			}
			if (i == 1) {
				searchMap.put(SENDER_UID, split[i]);				
			}
			if (i == 2) {
				searchMap.put(TARGET_PERSON_UID, split[i]);				
			}
			if (i == 3) {
				searchMap.put(TEMPLATE_ID, split[i]);				
			}
		}
		return searchMap;
	}
	
	/**
	 * Return number of received authorizations by recipient UID
	 * 
	 * @param recipientUserId
	 * @return number of recivied authorizations
	 * @throws KokuServiceException
	 */
	public int getUserRecievedWarrantCount(String recipientUserId) throws KokuServiceException {
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		criteria.setReceipientUid(recipientUserId);
		return service.getTotalAuthorizations(criteria);
	}
	
	/**
	 * Return number of total authorizartions by templateId, recipient UID and sender UID
	 * 
	 * @param authorizationTemplateId
	 * @param receipientUid
	 * @param senderUid
	 * @return
	 * @throws KokuServiceException
	 */
	public int getTotalAuthorizations(long authorizationTemplateId, String receipientUid, String senderUid) throws KokuServiceException {
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		criteria.setAuthorizationTemplateId(authorizationTemplateId);
		criteria.setReceipientUid(receipientUid);
		criteria.setSenderUid(senderUid);
		return service.getTotalAuthorizations(criteria);
	}

	/**
	 * Return authorization with additional details by given authorizationId
	 * 
	 * @param valtakirjaId
	 * @return Authorization with additional details
	 * @throws KokuServiceException
	 */
	public KokuAuthorizationSummary getAuthorizationDetails(int valtakirjaId) throws KokuServiceException {
		KokuAuthorizationSummary kokuSummary = new KokuAuthorizationSummary(service.getAuthorizationDetails(valtakirjaId));
		localize(kokuSummary);
		return kokuSummary;
	}
	
	private void localize(KokuAuthorizationSummary kokuSummary) throws KokuServiceException {
		kokuSummary.setLocalizedStatus(getLocalizedAuthStatus(kokuSummary.getStatus()));
	}

	/**
	 * Search authorization templates
	 * 
	 * @param searchString
	 * @param limit
	 * @return list of authorization templates
	 * @throws KokuServiceException
	 */
	public List<KokuValtakirjapohja> searchAuthorizationTemplates(String searchString, int limit) throws KokuServiceException {
		List<KokuValtakirjapohja> valtakirjas = new ArrayList<KokuValtakirjapohja>();
		List<fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja> valtakirjasFromService = service.searchAuthorizationTemplates(searchString, limit);
		for (fi.arcusys.koku.tiva.warrant.employeewarrantservice.Valtakirjapohja valtakirjapohja : valtakirjasFromService) {
			valtakirjas.add(new KokuValtakirjapohja(valtakirjapohja));
		}
		return valtakirjas;
	}

	/**
	 * Search authorizations by given authorization template name
	 * 
	 * @param keyword
	 * @param startNum
	 * @param maxNum
	 * @return list of authorizations
	 * @throws KokuServiceException
	 */
	public List<KokuAuthorizationSummary> getAuthorizationsByTemplateId(String keyword, int startNum, int maxNum) throws KokuServiceException {
		Map<String, String> searchMap = createTemplateSearchMap(keyword);
		if (searchMap == null) {
			return new ArrayList<KokuAuthorizationSummary>();
		}
		AuthorizationQuery query = new AuthorizationQuery();
		AuthorizationCriteria criteria = new AuthorizationCriteria();
		
		List<Valtakirjapohja> templates = service.searchAuthorizationTemplates(searchMap.get(TEMPLATE_NAME), MAX_SEARCH_RESULTS);
		long templateId = 0;
		if (templates != null && !templates.isEmpty()) {
			templateId = templates.get(0).getTemplateId();
		}
		
		try {
//			criteria.setAuthorizationTemplateId(Long.valueOf(searchMap.get(TEMPLATE_ID)));
			criteria.setAuthorizationTemplateId(templateId);
		} catch (NumberFormatException nfe) {
			// Catch silently. Leave templateId criteria empty 
		}
		if (criteria.getAuthorizationTemplateId() == null) {
			return new ArrayList<KokuAuthorizationSummary>();
		}
		query.setCriteria(criteria);
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);
		List<AuthorizationShortSummary> authShortSummaries = service.getAuthorizations(query);
		List<KokuAuthorizationSummary> summaries = new ArrayList<KokuAuthorizationSummary>();
		for (AuthorizationShortSummary shortSummary : authShortSummaries) {
			KokuAuthorizationSummary kokuSummary = new KokuAuthorizationSummary(shortSummary);
			localize(kokuSummary);
			summaries.add(kokuSummary);
		}
		return summaries;
	}
	
	private Map<String, String> createTemplateSearchMap(String keyword) {
		if (keyword == null) {
				return null;
		}
		
		Map<String, String> searchMap = new HashMap<String, String>(5);
		String[] split = keyword.split(SPLIT_REGEX);
		if (split.length == 0) {
			return null;
		}
		// FIXME: Not like this..  
		for (int i = 0; i < split.length; i++) {
			if (i == 0) {
				searchMap.put(TEMPLATE_NAME, split[i]);				
			}
			if (i == 1) {
				searchMap.put(FILTER, split[i]);				
			}
		}
		return searchMap;
	}
	
	
}
