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
	
	public List<CitizenConsent> getAssignedConsents(String user, int startNum, int maxNum) {
		TivaCitizenService tcs = new TivaCitizenService();
		List<ConsentShortSummary> consentSummary = tcs.getAssignedConsents(user, startNum, maxNum);
		List<CitizenConsent> consentList = new ArrayList<CitizenConsent>();
		CitizenConsent citizenConsent;		
		Iterator<ConsentShortSummary> it = consentSummary.iterator();
		
		while(it.hasNext()) {
			ConsentShortSummary consent = it.next();
			citizenConsent = new CitizenConsent();
			citizenConsent.setConsentId(consent.getConsentId());
			citizenConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
			citizenConsent.setRequester(consent.getRequestor());
			citizenConsent.setTemplateName(consent.getTemplateName());
			consentList.add(citizenConsent);
		}
		
		return consentList;
	}
	
	public CitizenConsent getConsentById(String consentIdStr) {
		long  consentId = (long) Long.parseLong(consentIdStr);
		TivaCitizenService tcs = new TivaCitizenService();
		CitizenConsent citizenConsent = new CitizenConsent();		
		ConsentTO consent = tcs.getConsentById(consentId);
		citizenConsent.setConsentId(consent.getConsentId());
		citizenConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
		citizenConsent.setRequester(consent.getRequestor());
		citizenConsent.setTemplateName(consent.getTemplateName());
		citizenConsent.setCreateType(consent.getCreateType().toString());
		citizenConsent.setStatus(consent.getStatus().toString());
		citizenConsent.setAssignedDate(MessageUtil.formatTaskDate(consent.getGivenAt()));
		citizenConsent.setValidDate(MessageUtil.formatTaskDate(consent.getValidTill()));
		citizenConsent.setActionRequests(convertActionRequests(consent.getActionRequests()));
		
		return citizenConsent;
	}
	
	public List<CitizenConsent> getOwnConsents(String user, int startNum, int maxNum) {
		TivaCitizenService tcs = new TivaCitizenService();
		List<ConsentSummary> consentSummary = tcs.getOwnConsents(user, startNum, maxNum);
		List<CitizenConsent> consentList = new ArrayList<CitizenConsent>();
		CitizenConsent citizenConsent;	
		Iterator<ConsentSummary> it = consentSummary.iterator();
		
		while(it.hasNext()) {
			ConsentSummary consent = it.next();
			citizenConsent = new CitizenConsent();
			citizenConsent.setConsentId(consent.getConsentId());
			citizenConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
			citizenConsent.setRequester(consent.getRequestor());
			citizenConsent.setTemplateName(consent.getTemplateName());
			citizenConsent.setCreateType(consent.getCreateType().toString());
			citizenConsent.setStatus(consent.getStatus().toString());
			citizenConsent.setAssignedDate(MessageUtil.formatTaskDate(consent.getGivenAt()));
			citizenConsent.setValidDate(MessageUtil.formatTaskDate(consent.getValidTill()));
			consentList.add(citizenConsent);
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
