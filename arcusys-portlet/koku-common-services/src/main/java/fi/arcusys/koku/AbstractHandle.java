package fi.arcusys.koku;

import org.springframework.context.support.ResourceBundleMessageSource;

public abstract class AbstractHandle {
	
	private ResourceBundleMessageSource messageSource;
	
	protected static final String MESSAGE_SOURCE_MISSING 	= "Coulnd't find localized message MessageSource missing. Localization doesn't work properly";
	protected static final String SPLIT_REGEX 				= "\\|";

	
	public final ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public final void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
