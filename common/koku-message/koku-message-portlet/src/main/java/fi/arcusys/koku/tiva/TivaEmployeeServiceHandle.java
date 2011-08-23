package fi.arcusys.koku.tiva;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fi.arcusys.koku.tiva.employeeservice.ActionRequestSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentCriteria;
import fi.arcusys.koku.tiva.employeeservice.ConsentQuery;
import fi.arcusys.koku.tiva.employeeservice.ConsentSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentTO;
import fi.arcusys.koku.tiva.employeeservice.ConsentTemplateSummary;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Handles tiva consents related operations for employee
 * @author Jinhua Chen
 * Aug 18, 2011
 */
public class TivaEmployeeServiceHandle {

	private TivaEmployeeService tes;
	
	/**
	 * Constructor and initialization
	 */
	public TivaEmployeeServiceHandle() {
		tes = new TivaEmployeeService();
	}
	
	/**
	 * Gets consents and generates koku consent data model for use
	 * @param user user name
	 * @param keyword keyword for filtering
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return
	 */
	public List<KokuConsent> getConsents(String user, String keyword, int startNum, int maxNum) {
		ConsentQuery query = new ConsentQuery();
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);		
		ConsentCriteria criteria = createCriteria(keyword);	
		query.setCriteria(criteria);		
		List<ConsentSummary> consentSummary = tes.getConsents(user,query);
		List<KokuConsent> consentList = new ArrayList<KokuConsent>();
		KokuConsent kokuConsent;	
		Iterator<ConsentSummary> it = consentSummary.iterator();
		
		while(it.hasNext()) {
			ConsentSummary consent = it.next();
			kokuConsent = new KokuConsent();
			kokuConsent.setConsentId(consent.getConsentId());
			kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
			kokuConsent.setRequester(consent.getRequestor());
			kokuConsent.setTemplateName(consent.getTemplateName());
			kokuConsent.setCreateType(consent.getCreateType().value());
			kokuConsent.setStatus(consent.getStatus().toString());
			kokuConsent.setAssignedDate(MessageUtil.formatTaskDate(consent.getGivenAt()));
			kokuConsent.setValidDate(MessageUtil.formatTaskDate(consent.getValidTill()));
			consentList.add(kokuConsent);
		}
		
		return consentList;
	}
	
	/**
	 * Gets total number of consents
	 * @param user user name
	 * @param keyword keyword for filtering
	 * @return the total number of consents
	 */
	public int getTotalConsents(String user, String keyword) {
		ConsentQuery query = new ConsentQuery();	
		ConsentCriteria criteria = createCriteria(keyword);	
		query.setCriteria(criteria);
		
		return tes.getTotalConsents(user, query);
	}
	
	/**
	 * Gets consent in detail
	 * @param consentIdStr consent id string
	 * @return detailed consent
	 */
	public KokuConsent getConsentDetails(String consentIdStr) {
		long  consentId = (long) Long.parseLong(consentIdStr);
		KokuConsent kokuConsent = new KokuConsent();		
		ConsentTO consent = tes.getConsentDetails(consentId);
		kokuConsent.setConsentId(consent.getConsentId());
		kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
		kokuConsent.setRequester(consent.getRequestor());
		kokuConsent.setTemplateName(consent.getTemplateName());
		kokuConsent.setCreateType(consent.getCreateType().value());
		kokuConsent.setStatus(consent.getStatus().toString());
		kokuConsent.setAssignedDate(MessageUtil.formatTaskDate(consent.getGivenAt()));
		kokuConsent.setValidDate(MessageUtil.formatTaskDate(consent.getValidTill()));
		kokuConsent.setActionRequests(convertActionRequests(consent.getActionRequests()));
		
		return kokuConsent;
	}
	
	/**
	 * Searches consent templates
	 * @param searchStr search string
	 * @param limit limited number of results
	 * @return a list of templates
	 */
	public List<ConsentTemplateSummary> searchConsentTemplates(String searchStr, int limit) {
		
		return tes.searchConsentTemplates(searchStr, limit);
	}
	
	/**
	 * Creates criteria for filtering consents
	 * @param keyword keyword string
	 * @return ConsentCriteria object
	 */
	private ConsentCriteria createCriteria(String keyword) {
		ConsentCriteria criteria = new ConsentCriteria();
		
		if(keyword.trim().length() > 0) {
			String[] keywords = keyword.split("_");
			String consentTemplateId = keywords[0];
			String receipientUid = keywords[1];
			criteria.setConsentTemplateId(Long.parseLong(consentTemplateId));
			criteria.setReceipientUid(receipientUid);
		}else {
			return null;
		}
		
		return criteria;
		
	}
	
	/**
	 * Converts the ActionRequestSummary object to ActionRequest
	 * @param actionSummaryList a list of ActionRequestSummary objects
	 * @return a list of ActionRequest objects
	 */
	private List<ActionRequest> convertActionRequests(List<ActionRequestSummary> actionSummaryList) {
		List<ActionRequest> actionList = new ArrayList<ActionRequest>();
		ActionRequest actionReq;
		Iterator<ActionRequestSummary> it = actionSummaryList.iterator();
		
		while(it.hasNext()) {
			ActionRequestSummary actionSummary = it.next();
			actionReq = new ActionRequest();
			actionReq.setDescription(actionSummary.getDescription());
			actionReq.setStatus(actionSummary.getStatus().toString());
			actionList.add(actionReq);
		}
		
		return actionList;	
	}
}
