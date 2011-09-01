package fi.arcusys.koku.tiva;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;

import fi.arcusys.koku.tiva.employeeservice.ConsentCreateType;
import fi.arcusys.koku.tiva.employeeservice.ActionRequestStatus;
import fi.arcusys.koku.tiva.employeeservice.ActionRequestSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentCriteria;
import fi.arcusys.koku.tiva.employeeservice.ConsentQuery;
import fi.arcusys.koku.tiva.employeeservice.ConsentStatus;
import fi.arcusys.koku.tiva.employeeservice.ConsentSummary;
import fi.arcusys.koku.tiva.employeeservice.ConsentTO;
import fi.arcusys.koku.tiva.employeeservice.SuostumuspohjaShort;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Handles tiva consents related operations for employee
 * @author Jinhua Chen
 * Aug 18, 2011
 */
public class TivaEmployeeServiceHandle {
	
	private Logger LOG = Logger.getLogger(TivaEmployeeServiceHandle.class);
	
	private ResourceBundleMessageSource messageSource;


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
			kokuConsent.setCreateType(localizeConsentCreateType(consent.getCreateType()));
			kokuConsent.setStatus(localizeConsentStatus(consent.getStatus()));
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
	public int getTotalConsents(String user, String keyword) {
		ConsentQuery query = new ConsentQuery();	
		ConsentCriteria criteria = createCriteria(keyword);	
		query.setCriteria(criteria);
		
		return tes.getTotalConsents(user);
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
		kokuConsent.setCreateType(localizeConsentCreateType(consent.getCreateType()));
		kokuConsent.setStatus(localizeConsentStatus(consent.getStatus()));
		kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
		kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
		kokuConsent.setActionRequests(convertActionRequests(consent.getActionRequests()));
		
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
			actionReq.setStatus(localizeActionRequestStatus(actionSummary.getStatus()));
			actionList.add(actionReq);
		}		
		return actionList;	
	}
	
	
	private String localizeConsentStatus(ConsentStatus consentStatus) {
		if (messageSource == null) {
			LOG.warn("MessageSource is null. Localization doesn't work properly");
			return consentStatus.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(consentStatus) {
			case DECLINED:
				return messageSource.getMessage("ConsentStatus.DECLINED", null, locale);
			case EXPIRED:
				return messageSource.getMessage("ConsentStatus.EXPIRED", null, locale);
			case OPEN:
				return messageSource.getMessage("ConsentStatus.OPEN", null, locale);
			case PARTIALLY_GIVEN:
				return messageSource.getMessage("ConsentStatus.PARTIALLY_GIVEN", null, locale);
			case REVOKED:
				return messageSource.getMessage("ConsentStatus.REVOKED", null, locale);
			case VALID:
				return messageSource.getMessage("ConsentStatus.VALID", null, locale);
			default:
				return messageSource.getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message. Localization doesn't work properly");
			return consentStatus.toString().toLowerCase();
		}
	}
	
	private String localizeActionRequestStatus(ActionRequestStatus actionRequestStatus) {
		if (messageSource == null) {
			LOG.warn("MessageSource is null. Localization doesn't work properly");
			return actionRequestStatus.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		try {
		switch(actionRequestStatus) {
		case DECLINED:
			return messageSource.getMessage("ConsentReplyStatus.DECLINED", null, locale);
		case GIVEN:
			return messageSource.getMessage("ConsentReplyStatus.GIVEN", null, locale);
		default:
			return messageSource.getMessage("unknown", null, locale);
		}
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message. Localization doesn't work properly");
			return actionRequestStatus.toString().toLowerCase();
		}
	}
	
	private String localizeConsentCreateType(ConsentCreateType type) {
		if (messageSource == null) {
			LOG.warn("MessageSource is null. Localization doesn't work properly");
			return type.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(type) {
			case ELECTRONIC:
				return messageSource.getMessage("ConsentType.Electronic", null, locale);
			case EMAIL_BASED:
				return messageSource.getMessage("ConsentType.PaperBased", null, locale);
			case PAPER_BASED:
				return messageSource.getMessage("ConsentType.EmailBased", null, locale);
			default:
				return messageSource.getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message. Localization doesn't work properly");
			return type.toString().toLowerCase();
		}
	}

	public final ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public final void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
}
