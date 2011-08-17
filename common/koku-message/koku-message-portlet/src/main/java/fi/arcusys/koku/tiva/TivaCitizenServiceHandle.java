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
 * 
 * @author Jinhua Chen
 * Aug 15, 2011
 */
public class TivaCitizenServiceHandle {
	
	public TivaCitizenServiceHandle() {}
	
	public List<KokuConsent> getAssignedConsents(String user, int startNum, int maxNum) {
		TivaCitizenService tcs = new TivaCitizenService();
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
	
	public KokuConsent getConsentById(String consentIdStr) {
		long  consentId = (long) Long.parseLong(consentIdStr);
		TivaCitizenService tcs = new TivaCitizenService();
		KokuConsent kokuConsent = new KokuConsent();		
		ConsentTO consent = tcs.getConsentById(consentId);
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
	
	public List<KokuConsent> getOwnConsents(String user, int startNum, int maxNum) {
		TivaCitizenService tcs = new TivaCitizenService();
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
			kokuConsent.setStatus(consent.getStatus().toString());
			kokuConsent.setAssignedDate(MessageUtil.formatTaskDate(consent.getGivenAt()));
			kokuConsent.setValidDate(MessageUtil.formatTaskDate(consent.getValidTill()));
			consentList.add(kokuConsent);
		}
		
		return consentList;
	}
	
	public int getTotalAssignedConsents(String user) {
		TivaCitizenService tcs = new TivaCitizenService();
		
		return tcs.getTotalAssignedConsents(user);
	}
	
	public int getTotalOwnConsents(String user) {
		TivaCitizenService tcs = new TivaCitizenService();
		
		return tcs.getTotalOwnConsents(user);
	}
	
	public String revokeOwnConsent(String consentIdStr) {
		TivaCitizenService tcs = new TivaCitizenService();
		long  consentId = (long) Long.parseLong(consentIdStr);
		
		try {
			tcs.revokeOwnConsent(consentId);
			return MessageUtil.RESPONSE_OK;
		} catch(RuntimeException e) {
			return MessageUtil.RESPONSE_FAIL;
		}
	}
	
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
