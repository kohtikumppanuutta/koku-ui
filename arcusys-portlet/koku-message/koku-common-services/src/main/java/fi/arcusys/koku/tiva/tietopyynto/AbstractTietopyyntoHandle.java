package fi.arcusys.koku.tiva.tietopyynto;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationAccessType;
import fi.arcusys.koku.tiva.tietopyynto.model.KokuInformationRequestStatus;
import fi.arcusys.koku.util.MessageUtil;

public class AbstractTietopyyntoHandle extends AbstractHandle {

	private Logger LOG = Logger.getLogger(AbstractTietopyyntoHandle.class);

	protected String getLocalizedInformationRequestSummary(KokuInformationRequestStatus type) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(type) {
			case VALID:
				return getMessageSource().getMessage("InformationRequestStatus.VALID", null, locale);
			case DECLINED:
				return getMessageSource().getMessage("InformationRequestStatus.DECLINED", null, locale);
			case EXPIRED:
				return getMessageSource().getMessage("InformationRequestStatus.EXPIRED", null, locale);
			case OPEN:
				return getMessageSource().getMessage("InformationRequestStatus.OPEN", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
	}
	
	
	protected String getLocalizedInfoAccessType(KokuInformationAccessType type) {
		if (getMessageSource() == null) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {
			switch(type) {
			case MANUAL:
				return getMessageSource().getMessage("InformationAccessType.PORTAL", null, locale);
			case PORTAL:
				return getMessageSource().getMessage("InformationAccessType.MANUAL", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn(MESSAGE_SOURCE_MISSING);
			return type.toString().toLowerCase();
		}
	}
	
}
