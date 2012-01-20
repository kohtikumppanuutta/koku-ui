package fi.arcusys.koku.tiva;

import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.tiva.citizenservice.ActionRequestStatus;
import fi.arcusys.koku.tiva.citizenservice.ActionRequestSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentApprovalStatus;
import fi.arcusys.koku.tiva.citizenservice.ConsentCreateType;
import fi.arcusys.koku.tiva.citizenservice.ConsentShortSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentStatus;
import fi.arcusys.koku.tiva.citizenservice.ConsentSummary;
import fi.arcusys.koku.tiva.citizenservice.ConsentTO;
import fi.arcusys.koku.util.MessageUtil;
import fi.arcusys.koku.util.PortalRole;

/**
 * Handles tiva consents related operations for citizen
 * @author Jinhua Chen
 * Aug 15, 2011
 */
public class TivaCitizenServiceHandle extends AbstractHandle {
	
	private Logger LOG = Logger.getLogger(TivaCitizenServiceHandle.class);
	
	private TivaCitizenService tcs;
	private String userId;
	
	/**
	 * Constructor and initialization
	 */
	public TivaCitizenServiceHandle() {
		tcs = new TivaCitizenService();
	}
	
	public TivaCitizenServiceHandle(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("userId is null!");
		}
		this.userId = userId;
		tcs = new TivaCitizenService();
	}
	
	/**
	 * Gets assigned consents and generates koku consent data model for use
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of assigned consents
	 */
	public List<KokuConsent> getAssignedConsents(String userId, int startNum, int maxNum) throws KokuServiceException {
		List<ConsentShortSummary> consentSummary = tcs.getAssignedConsents(userId, startNum, maxNum);
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
			kokuConsent.setTemplateTypeName(consent.getTemplateTypeName());
			kokuConsent.setReplyTill(MessageUtil.formatTaskDateByDay(consent.getReplyTill()));
			consentList.add(kokuConsent);
		}
		return consentList;
	}
	
	/**
	 * Gets consent in detail and generates koku consent data model for use
	 * @param consentIdStr consent id string
	 * @return detailed consent
	 */
	public KokuConsent getConsentById(String consentIdStr) throws KokuServiceException {
		long  consentId = 0;
		try {
			consentId = (long) Long.parseLong(consentIdStr);			
		} catch (NumberFormatException nfe) {
			LOG.warn("Invalid ConsentId. ConsentId: '"+consentIdStr+"'");
			return null;
		}
		KokuConsent kokuConsent = new KokuConsent();
		ConsentTO consent = tcs.getConsentById(consentId, this.userId);
		kokuConsent.setConsentId(consent.getConsentId());
		kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
		kokuConsent.setRequester(consent.getRequestor());
		kokuConsent.setTemplateName(consent.getTemplateName());
		kokuConsent.setCreateType(localizeConsentCreateType(consent.getCreateType()));
		if(consent.getStatus() != null) {
			kokuConsent.setStatus(localizeConsentStatus(consent.getStatus()));
		}
		kokuConsent.setApprovalStatus(localizeApprovalConsentStatus(consent.getApprovalStatus()));		
		kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
		kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
		kokuConsent.setActionRequests(convertActionRequests(consent.getActionRequests()));
		kokuConsent.setReplyTill(MessageUtil.formatTaskDateByDay(consent.getReplyTill()));
		kokuConsent.setComment(consent.getComment());
		kokuConsent.setTemplateTypeName(consent.getTemplateTypeName());
		return kokuConsent;
	}
	
	
	
	/**
	 * Gets own consents and generates koku consent data model for use
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of consents
	 */
	public List<KokuConsent> getOwnConsents(String userId, int startNum, int maxNum) throws KokuServiceException {
		return convertConsentsToKokuConsents(tcs.getOwnConsents(userId, startNum, maxNum));
	}
	
	/**
	 * Gets own old consents and generates koku consent data model for use
	 * @param user user name
	 * @param startNum start index of consent
	 * @param maxNum maximum number of consents
	 * @return a list of consents
	 */
	public List<KokuConsent> getOwnOldConsents(String userId, int startNum, int maxNum) throws KokuServiceException {
		return convertConsentsToKokuConsents(tcs.getOldConsents(userId, startNum, maxNum));
	}
	
	private List<KokuConsent> convertConsentsToKokuConsents(List<ConsentSummary> consentSummary) {
		List<KokuConsent> consentList = new ArrayList<KokuConsent>();
		Iterator<ConsentSummary> it = consentSummary.iterator();		
		while(it.hasNext()) {
			ConsentSummary consent = it.next();			
			consentList.add(convertConsentToKokuConsent(consent));
		}		
		return consentList;
	}
	
	private KokuConsent convertConsentToKokuConsent(ConsentSummary consent) {
		KokuConsent kokuConsent = new KokuConsent();
		kokuConsent.setConsentId(consent.getConsentId());
		kokuConsent.setAnotherPermitterUid(consent.getAnotherPermitterUid());
		kokuConsent.setRequester(consent.getRequestor());
		kokuConsent.setTemplateTypeName(consent.getTemplateTypeName());
		kokuConsent.setTemplateName(consent.getTemplateName());
		kokuConsent.setCreateType(localizeConsentCreateType(consent.getCreateType()));
		if(consent.getStatus() != null) {
			kokuConsent.setStatus(localizeConsentStatus(consent.getStatus()));
		}
		kokuConsent.setApprovalStatus(localizeApprovalConsentStatus(consent.getApprovalStatus()));			
		kokuConsent.setAssignedDate(MessageUtil.formatTaskDateByDay(consent.getGivenAt()));
		kokuConsent.setValidDate(MessageUtil.formatTaskDateByDay(consent.getValidTill()));
		kokuConsent.setReplyTill(MessageUtil.formatTaskDateByDay(consent.getReplyTill()));
		return kokuConsent;
	}
	
	
	/**
	 * Gets the total number of assigned consents
	 * @param user user name
	 * @return the total number of assigned consents
	 */
	public int getTotalAssignedConsents(String userId) throws KokuServiceException {
		return tcs.getTotalAssignedConsents(userId);
	}
	
	/**
	 * Gets the total number of own consents
	 * @param user user name
	 * @return the total number of own consents
	 */
	public int getTotalOwnConsents(String userId) throws KokuServiceException {
		return tcs.getTotalOwnConsents(userId);
	}
	
	/**
	 * Gets the total number of old own consents
	 * @param user user name
	 * @return the total number of old own consents
	 */
	public int getTotalOwnOldConsents(String userId) throws KokuServiceException {
		return tcs.getTotalOldConsents(userId);
	}
	
	/**
	 * Revokes the consent
	 * @param consentIdStr consent id string
	 * @return operation response
	 */
	public String revokeOwnConsent(String consentIdStr) throws KokuServiceException {
		long consentId = 0;		
		try {
			consentId = (long) Long.parseLong(consentIdStr);
		} catch (NumberFormatException nfe) {
			LOG.warn("Invalid consentId. ConsentId: '"+consentIdStr+"'");
			return RESPONSE_FAIL;
		}
		
		try {
			tcs.revokeOwnConsent(consentId, this.userId);
			return RESPONSE_OK;
		} catch(RuntimeException e) {
			LOG.error("Failed to revoke consent. ConsentId: '"+consentIdStr+"'");
			return RESPONSE_FAIL;
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
			actionReq.setStatus(localizeActionRequestStatus(actionSummary.getStatus()));
			actionReq.setName(actionSummary.getName());
			actionList.add(actionReq);
		}
		
		return actionList;	
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
	
	
	private String localizeConsentStatus(ConsentStatus consentStatus) {
		Locale locale = MessageUtil.getLocale();
		
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return consentStatus.toString().toLowerCase();
		}
		
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
				return getMessageSource().getMessage("ConsentType.EmailBased", null, locale);
			case PAPER_BASED:
				return getMessageSource().getMessage("ConsentType.PaperBased", null, locale);
            case VERBAL:
                return getMessageSource().getMessage("ConsentType.Verbal", null, locale);
            case FAX:
                return getMessageSource().getMessage("ConsentType.Fax", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
	}
	
}
