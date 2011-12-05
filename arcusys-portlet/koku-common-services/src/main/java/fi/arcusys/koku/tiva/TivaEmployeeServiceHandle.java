package fi.arcusys.koku.tiva;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.tiva.employeeservice.ConsentApprovalStatus;
import fi.arcusys.koku.tiva.employeeservice.ConsentCreateType;
import fi.arcusys.koku.tiva.employeeservice.ActionRequestStatus;
import fi.arcusys.koku.tiva.employeeservice.ActionRequestSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentCriteria;
import fi.arcusys.koku.tiva.employeeservice.ConsentQuery;
import fi.arcusys.koku.tiva.employeeservice.ConsentStatus;
import fi.arcusys.koku.tiva.employeeservice.ConsentSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentTO;
import fi.arcusys.koku.tiva.employeeservice.SuostumuspohjaShort;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.util.MessageUtil;
import fi.arcusys.koku.util.PortalRole;

/**
 * Handles tiva consents related operations for employee
 * @author Jinhua Chen
 * Aug 18, 2011
 */
public class TivaEmployeeServiceHandle extends AbstractHandle {
	
	private Logger LOG = Logger.getLogger(TivaEmployeeServiceHandle.class);
	
	private TivaEmployeeService tes;
	
	/**
	 * Constructor and initialization
	 */
	public TivaEmployeeServiceHandle() {
		tes = new TivaEmployeeService();
	}
	
	/**
	 * Gets consents and generates koku consent data model for use
	 * @param username user name
	 * @param keyword keyword for filtering
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return
	 */
	public List<KokuConsent> getConsents(String userId, String keyword, String field, int startNum, int maxNum) {
		if (userId == null) {
			throw new IllegalArgumentException("userId can't be null");
		}
		ConsentQuery query = new ConsentQuery();
		query.setStartNum(startNum);
		query.setMaxNum(maxNum);		
		ConsentCriteria criteria = createCriteria(keyword, field);	
		query.setCriteria(criteria);		
		List<ConsentSummary> consentSummary = tes.getConsents(userId, query);
		List<KokuConsent> consentList = new ArrayList<KokuConsent>();
		
		if(consentSummary == null) {
			return consentList;
		}
		
		KokuConsent kokuConsent;	
		Iterator<ConsentSummary> it = consentSummary.iterator();
		
		while(it.hasNext()) {
			ConsentSummary consent = it.next();
			kokuConsent = new KokuConsent();
			kokuConsent.setConsentId(consent.getConsentId());
			kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
			kokuConsent.setRequester(consent.getRequestor());
			kokuConsent.setTemplateName(consent.getTemplateName());
			kokuConsent.setCreateType(localizeConsentCreateType(consent.getCreateType()));
			kokuConsent.setStatus(localizeConsentStatus(consent.getStatus()));
			kokuConsent.setApprovalStatus(localizeApprovalConsentStatus(consent.getApprovalStatus()));
			kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
			kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
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
	public int getTotalConsents(String user, String keyword, String field) {
		ConsentQuery query = new ConsentQuery();	
		ConsentCriteria criteria = createCriteria(keyword, field);	
		query.setCriteria(criteria);
		
		return tes.getTotalConsents(user, criteria);
	}
	
	/**
	 * Gets consent in detail
	 * @param consentIdStr consent id string
	 * @return detailed consent
	 */
	public KokuConsent getConsentDetails(String consentIdStr) {
		long  consentId = 0;
		try {
			consentId = (long) Long.parseLong(consentIdStr);
		} catch (NumberFormatException nfe) {
			LOG.warn("Invalid consentId. ConsentId: '"+consentIdStr+"'");
			return null;
		}
		KokuConsent kokuConsent = new KokuConsent();		
		ConsentTO consent = tes.getConsentDetails(consentId);
		kokuConsent.setConsentId(consent.getConsentId());
		kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
		kokuConsent.setRequester(consent.getRequestor());
		kokuConsent.setTemplateName(consent.getTemplateName());
		kokuConsent.setCreateType(localizeConsentCreateType(consent.getCreateType()));
		kokuConsent.setStatus(localizeConsentStatus(consent.getStatus()));
		kokuConsent.setApprovalStatus(localizeApprovalConsentStatus(consent.getApprovalStatus()));
		kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
		kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
		kokuConsent.setActionRequests(convertActionRequests(consent.getActionRequests()));
		kokuConsent.setRecipients(MessageUtil.formatRecipients(consent.getReceipients()));
		// TODO: Get comment from WS
		kokuConsent.setComment(null);
			
		return kokuConsent;
	}
	
	/**
	 * Searches consent templates
	 * @param searchStr search string
	 * @param limit limited number of results
	 * @return a list of templates
	 */
	public List<SuostumuspohjaShort> searchConsentTemplates(String searchStr, int limit) {
		
		return tes.searchConsentTemplates(searchStr, limit);
	}
	
	/**
	 * Creates criteria for filtering consents
	 * @param keyword keyword string
	 * @return ConsentCriteria object
	 */
	private ConsentCriteria createCriteria(String keyword, String field) {
		ConsentCriteria criteria = new ConsentCriteria();
		
		criteria.setReceipientUid(keyword);

		if(field.trim().length() > 0) {
			try {
				criteria.setConsentTemplateId(Long.parseLong(field));				
			} catch (NumberFormatException nfe) {
				LOG.warn("Invalid field. Creating criteria for consent filtering failed. Field: '"+field+"'");
				return null;
			}
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
			actionReq.setStatus(localizeActionRequestStatus(actionSummary.getStatus()));
			actionReq.setName(actionSummary.getName());
			actionList.add(actionReq);
		}		
		return actionList;	
	}
	
	
	private String localizeConsentStatus(ConsentStatus consentStatus) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return consentStatus.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(consentStatus) {
			case DECLINED:
				return getMessageSource().getMessage("ConsentStatus.DECLINED", null, locale);
			case EXPIRED:
				return getMessageSource().getMessage("ConsentStatus.EXPIRED", null, locale);
			case OPEN:
				return getMessageSource().getMessage("ConsentStatus.OPEN", null, locale);
			case PARTIALLY_GIVEN:
				return getMessageSource().getMessage("ConsentStatus.PARTIALLY_GIVEN", null, locale);
			case REVOKED:
				return getMessageSource().getMessage("ConsentStatus.REVOKED", null, locale);
			case VALID:
				return getMessageSource().getMessage("ConsentStatus.VALID", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return consentStatus.toString().toLowerCase();
		}
	}
	
	private String localizeActionRequestStatus(ActionRequestStatus actionRequestStatus) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return actionRequestStatus.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		try {
		switch(actionRequestStatus) {
		case DECLINED:
			return getMessageSource().getMessage("ConsentReplyStatus.DECLINED", null, locale);
		case GIVEN:
			return getMessageSource().getMessage("ConsentReplyStatus.GIVEN", null, locale);
		default:
			return getMessageSource().getMessage("unknown", null, locale);
		}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return actionRequestStatus.toString().toLowerCase();
		}
	}
	
	private String localizeConsentCreateType(ConsentCreateType type) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(type) {
			case ELECTRONIC:
				return getMessageSource().getMessage("ConsentType.Electronic", null, locale);
			case EMAIL_BASED:
				return getMessageSource().getMessage("ConsentType.PaperBased", null, locale);
			case PAPER_BASED:
				return getMessageSource().getMessage("ConsentType.EmailBased", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
	}
	
	private String localizeApprovalConsentStatus(ConsentApprovalStatus approvalStatus) {
		Locale locale = MessageUtil.getLocale();
		
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return approvalStatus.toString().toLowerCase();
		}
		
		try {			
			switch(approvalStatus) {
			case DECLINED:
				return getMessageSource().getMessage("ApprovalConsentStatus.DECLINED", null, locale);
			case APPROVED:
				return getMessageSource().getMessage("ApprovalConsentStatus.APPROVED", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return approvalStatus.toString().toLowerCase();
		}
	}

	
}
