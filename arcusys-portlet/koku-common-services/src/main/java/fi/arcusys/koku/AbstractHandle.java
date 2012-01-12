package fi.arcusys.koku;

import org.springframework.context.MessageSource;

public abstract class AbstractHandle {
	
	private MessageSource messageSource;
	
	protected static final String MESSAGE_SOURCE_MISSING 	= "Coulnd't find localized message MessageSource missing. Localization doesn't work properly";
	protected static final String SPLIT_REGEX 				= "\\|";

	
	public final MessageSource getMessageSource() {
		return messageSource;
	}

	public final void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
