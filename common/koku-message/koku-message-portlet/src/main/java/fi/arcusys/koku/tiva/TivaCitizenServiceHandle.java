package fi.arcusys.koku.tiva;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fi.arcusys.koku.tiva.citizenservice.ActionRequestSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentShortSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentTO;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Handles tiva consents related operations for citizen
 * @author Jinhua Chen
 * Aug 15, 2011
 */
public class TivaCitizenServiceHandle {
	
	private TivaCitizenService tcs;
	private String user;
	
	/**
	 * Constructor and initialization
	 */
	public TivaCitizenServiceHandle() {
		tcs = new TivaCitizenService();
	}
	
	public TivaCitizenServiceHandle(String user) {
		this.user = user;
		tcs = new TivaCitizenService();
	}
	
	/**
	 * Gets assigned consents and generates koku consent data model for use
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of assigned consents
	 */
	public List<KokuConsent> getAssignedConsents(String user, int startNum, int maxNum) {
		List<ConsentShortSummary> consentSummary = tcs.getAssignedConsents(user, startNum, maxNum);
		List<KokuConsent> consentList = new ArrayList<KokuConsent>();
		KokuConsent kokuConsent;		
		Iterator<ConsentShortSummary> it = consentSummary.iterator();
		
		while(it.hasNext()) {
			ConsentShortSummary consent = it.next();
			kokuConsent = new KokuConsent();
			kokuConsent.setConsentId(consent.getConsentId());
			kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
			kokuConsent.setRequester(consent.getRequestor());
			kokuConsent.setTemplateName(consent.getTemplateName());
			consentList.add(kokuConsent);
		}
		
		return consentList;
	}
	
	/**
	 * Gets consent in detail and generates koku consent data model for use
	 * @param consentIdStr consent id string
	 * @return detailed consent
	 */
	public KokuConsent getConsentById(String consentIdStr) {
		long  consentId = (long) Long.parseLong(consentIdStr);
		KokuConsent kokuConsent = new KokuConsent();		
		ConsentTO consent = tcs.getConsentById(consentId, user);
		kokuConsent.setConsentId(consent.getConsentId());
		kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
		kokuConsent.setRequester(consent.getRequestor());
		kokuConsent.setTemplateName(consent.getTemplateName());
		kokuConsent.setCreateType(consent.getCreateType().value());
		if(consent.getStatus() != null) {
			kokuConsent.setStatus(consent.getStatus().toString());
		}
		
		kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
		kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
		kokuConsent.setActionRequests(convertActionRequests(consent.getActionRequests()));
		
		return kokuConsent;
	}
	
	/**
	 * Gets own consents and generates koku consent data model for use
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of consents
	 */
	public List<KokuConsent> getOwnConsents(String user, int startNum, int maxNum) {
		List<ConsentSummary> consentSummary = tcs.getOwnConsents(user, startNum, maxNum);
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
			if(consent.getStatus() != null) {
				kokuConsent.setStatus(consent.getStatus().toString());
			}
			kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
			kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
			consentList.add(kokuConsent);
		}
		
		return consentList;
	}
	
	/**
	 * Gets the total number of assigned consents
	 * @param user user name
	 * @return the total number of assigned consents
	 */
	public int getTotalAssignedConsents(String user) {
		
		return tcs.getTotalAssignedConsents(user);
	}
	
	/**
	 * Gets the total number of own consents
	 * @param user user name
	 * @return the total number of own consents
	 */
	public int getTotalOwnConsents(String user) {
		
		return tcs.getTotalOwnConsents(user);
	}
	
	/**
	 * Revokes the consent
	 * @param consentIdStr consent id string
	 * @return operation response
	 */
	public String revokeOwnConsent(String consentIdStr) {
		long  consentId = (long) Long.parseLong(consentIdStr);
		
		try {
			tcs.revokeOwnConsent(consentId, user);
			return MessageUtil.RESPONSE_OK;
		} catch(RuntimeException e) {
			return MessageUtil.RESPONSE_FAIL;
		}
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
