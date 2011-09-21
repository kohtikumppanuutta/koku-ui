package fi.arcusys.koku.tiva.warrant.citizens;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.tiva.citizenservice.ConsentCreateType;
import fi.arcusys.koku.tiva.warrant.citizenwarrantservice.AuthorizationShortSummary;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationCreateType;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationStatus;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationSummary;
import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.util.MessageUtil;


public class KokuCitizenWarrantHandle extends AbstractHandle {

	private Logger LOG = Logger.getLogger(KokuCitizenWarrantHandle.class);

	private KokuCitizenWarrantService service;
	
	private KokuUserService userService;
	
	
	/**
	 * Constructor and initialization
	 */
	public KokuCitizenWarrantHandle() {
		service = new KokuCitizenWarrantService();
		userService = new KokuUserService();
	}

	public List<KokuAuthorizationSummary> getSentWarrants(String userId, int startNum, int maxNum) {
		 List<AuthorizationShortSummary> summaries = service.getSentAuthorizations(userId, startNum, maxNum);
		 List<KokuAuthorizationSummary> summariesModels = new ArrayList<KokuAuthorizationSummary>();
		 for (AuthorizationShortSummary summary : summaries) {
			 KokuAuthorizationSummary shortSummary = new KokuAuthorizationSummary(summary);
			 shortSummary.setLocalizedStatus(localizeAuthStatus(shortSummary.getStatus()));
			 summariesModels.add(shortSummary);
		 }
		 return summariesModels;
	}
	
	public List<KokuAuthorizationSummary> getReceivedAuthorizations(String userId, int startNum, int maxNum) {
		 List<AuthorizationShortSummary> summaries = service.getSentAuthorizations(userId, startNum, maxNum);
		 List<KokuAuthorizationSummary> summariesModels = new ArrayList<KokuAuthorizationSummary>();
		 for (AuthorizationShortSummary summary : summaries) {
			 KokuAuthorizationSummary shortSummary = new KokuAuthorizationSummary(summary);
			 shortSummary.setLocalizedStatus(localizeAuthStatus(shortSummary.getStatus()));
			 summariesModels.add(shortSummary);
		 }
		 return summariesModels;	
	}
	
	public KokuAuthorizationSummary getAuthorizationSummaryById(long authorizationId, String userId) {
		
		KokuAuthorizationSummary summary = new KokuAuthorizationSummary(service.getAuthorizationSummaryById(authorizationId, userId));
		if (summary.getStatus() != null) {
			summary.setLocalizedStatus(localizeAuthStatus(summary.getStatus()));
		}
		if (summary.getType() != null) {
			summary.setLocalizedType(localizeWarrantCreateType(summary.getType()));
		}
		if (summary.getSenderUid() != null && summary.getReceiverUid() != null) {
			summary.setSenderName(userService.getUserUidByKunpoName(summary.getSenderUid()));
			summary.setRecieverName(userService.getUserUidByKunpoName(summary.getReceiverUid()));
		}
		return summary;
	}
	
	public int getTotalSentAuthorizations(String userId) {
		return service.getTotalSentAuthorizations(userId);
	}
	
	public int getTotalReceivedAuthorizations(String userId) {
		return service.getTotalReceivedAuthorizations(userId);
	}
	
	public void revokeOwnAuthorization(long authorizationId, String user, String comment) {
		service.revokeOwnAuthorization(authorizationId, user, comment);
	}
	
	private String localizeAuthStatus(KokuAuthorizationStatus type) {
		if (getMessageSource() == null) {
			LOG.warn("getMessageSource() is null. Localization doesn't work properly");
			return type.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(type) {
			case VALID:
				return getMessageSource().getMessage("AuthorizationStatus.VALID", null, locale);
			case DECLINED:
				return getMessageSource().getMessage("AuthorizationStatus.DECLINED", null, locale);
			case EXPIRED:
				return getMessageSource().getMessage("AuthorizationStatus.EXPIRED", null, locale);
			case OPEN:
				return getMessageSource().getMessage("AuthorizationStatus.OPEN", null, locale);
			case REVOKED:
				return getMessageSource().getMessage("AuthorizationStatus.REVOKED", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message. Localization doesn't work properly");
			return type.toString().toLowerCase();
		}
	}
	
	private String localizeWarrantCreateType(KokuAuthorizationCreateType  type) {
		if (getMessageSource() == null) {
			LOG.warn("getMessageSource() is null. Localization doesn't work properly");
			return type.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(type) {
			case ELECTRONIC:
				return getMessageSource().getMessage("AuthorizationType.ELECTRONIC", null, locale);
			case NON_ELECTRONIC:
				return getMessageSource().getMessage("AuthorizationType.NON_ELECTRONIC", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message. Localization doesn't work properly");
			return type.toString().toLowerCase();
		}
	}
	
}
