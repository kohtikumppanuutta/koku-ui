package fi.arcusys.koku.navi.util.impl;

import org.springframework.context.MessageSource;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.intalio.TaskHandle;
import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.model.KokuFolderType;
import fi.arcusys.koku.navi.util.QueryProcess;

public abstract class AbstractQueryProcess implements QueryProcess {
		
	private MessageSource messageSource;
	
	public AbstractQueryProcess(MessageSource messages) {
		this.messageSource = messages;
	}
	
	
	public final void setMessageSource(MessageSource messages) {
		this.messageSource = messages;
	}

	
	public final MessageSource getMessageSource() {
		return messageSource;
	}
	

	/**
	 * Returns number of new messages in the given folder type from web services
	 * 
	 * @param userId
	 * @param folderType
	 * @return number of messages
	 * @throws KokuServiceException 
	 */
	protected int getNewMessageNum(String userId, KokuFolderType folderType) throws KokuServiceException {
		MessageHandle messageHandle = new MessageHandle();
		return messageHandle.getUnreadMessages(userId, folderType);
	}
	
	/*
	 * Returns total amount of tasks 
	 * 
	 * @param userId
	 * @param token (from intalio)
	 * @filter filttering string
	 * @return number or requests
	 */
	protected int getTotalTasks(final String username, final String token, final String filter) {
		if (username != null && !username.isEmpty() && token != null && !token.isEmpty()) {
			TaskHandle handle = new TaskHandle(token, username);
			return handle.getTasksTotalNumber(filter);
		} else {
			return 0;
		}
	}

}
