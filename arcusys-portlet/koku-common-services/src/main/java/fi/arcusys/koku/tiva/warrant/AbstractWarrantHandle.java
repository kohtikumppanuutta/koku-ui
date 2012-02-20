package fi.arcusys.koku.tiva.warrant;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationCreateType;
import fi.arcusys.koku.tiva.warrant.model.KokuAuthorizationStatus;
import fi.arcusys.koku.util.MessageUtil;

public class AbstractWarrantHandle extends AbstractHandle {
	
	private static final Logger LOG = Logger.getLogger(AbstractWarrantHandle.class);

	protected String getLocalizedAuthStatus(KokuAuthorizationStatus type) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
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
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
	}
	
	protected String getLocalizedWarrantCreateType(KokuAuthorizationCreateType  type) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
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
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
	}

}
