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

public class TivaEmployeeServiceHandle {

	public TivaEmployeeServiceHandle() {}
	
	public List<KokuConsent> getConsents(String user, String keyword, int startNum, int maxNum) {
		TivaEmployeeService tes = new TivaEmployeeService();
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
	
	public int getTotalConsents(String user, String keyword) {
		TivaEmployeeService tes = new TivaEmployeeService();
		ConsentQuery query = new ConsentQuery();	
		ConsentCriteria criteria = createCriteria(keyword);	
		query.setCriteria(criteria);
		
		return tes.getTotalConsents(user, query);
	}
	
	public KokuConsent getConsentDetails(String consentIdStr) {
		long  consentId = (long) Long.parseLong(consentIdStr);
		TivaEmployeeService tes = new TivaEmployeeService();
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
	
	public List<ConsentTemplateSummary> searchConsentTemplates(String searchStr, int limit) {
		TivaEmployeeService tes = new TivaEmployeeService();
		
		return tes.searchConsentTemplates(searchStr, limit);
	}
	
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
