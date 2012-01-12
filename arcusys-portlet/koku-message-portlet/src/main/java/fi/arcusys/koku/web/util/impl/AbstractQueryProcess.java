package fi.arcusys.koku.web.util.impl;

import org.springframework.context.MessageSource;

import fi.arcusys.koku.web.util.QueryProcess;

public abstract class AbstractQueryProcess implements QueryProcess {
	
	private MessageSource messageSource;
	
	public AbstractQueryProcess(MessageSource messages) {
		this.messageSource = messages;
	}
	
	@Override
	public final void setMessageSource(MessageSource messages) {
		this.messageSource = messages;
	}

	@Override
	public final MessageSource getMessageSource() {
		return messageSource;
	}

}
