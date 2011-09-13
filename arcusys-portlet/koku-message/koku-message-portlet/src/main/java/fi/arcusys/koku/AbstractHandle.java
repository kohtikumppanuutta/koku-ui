package fi.arcusys.koku;

import org.springframework.context.support.ResourceBundleMessageSource;

public abstract class AbstractHandle {
	
	private ResourceBundleMessageSource messageSource;
	

	public final ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public final void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
