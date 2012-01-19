package fi.arcusys.koku.web.util.impl;

import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.kv.message.MessageHandle;
import fi.arcusys.koku.kv.messageservice.FolderType;
import fi.arcusys.koku.web.util.KokuActionProcess;
import fi.arcusys.koku.web.util.exception.KokuActionProcessException;


public abstract class AbstractKokuActionProcess implements KokuActionProcess  {
		
	private MessageHandle msghandle = null;
	private final String userId;
	
	public AbstractKokuActionProcess(String userId) {
		this.userId = userId;
	}	
	
	@Override
	public void archiveOldMessages(String folderType) throws KokuActionProcessException {
		
		if (folderType == null) {
			throw new KokuActionProcessException("FolderType is null!");
		}
		
		if (msghandle == null) {
			msghandle = new MessageHandle();
		}
		final String oldArchResult;
		try {
			oldArchResult = msghandle.archiveOldMessages(getUserId(), FolderType.fromValue(folderType));			
		} catch (RuntimeException e) {
			throw new KokuActionProcessException("Archiving old messages failed.", e);
		}
		if (oldArchResult == null || oldArchResult.equals(RESPONSE_FAIL)) {
			throw new KokuActionProcessException("Archiving old messages failed.");
		}
	}

	@Override
	public void deleteMessages(String[] messageIds) throws KokuActionProcessException {

		if (messageIds == null) {
			throw new KokuActionProcessException("Deleting message(s) failed. messageIds parameter is null");
		}
		
		if (msghandle == null) {
			msghandle = new MessageHandle();
		}
		
		List<Long> msgIds = new ArrayList<Long>();		
		for(String msgId : messageIds) {
			try {
				msgIds.add(Long.parseLong(msgId));
			} catch (NumberFormatException nfe) {
				throw new KokuActionProcessException("Couldn't delete message(s)! Invalid messageId. MessageId: '"+ msgId + "'");
			}
		}
		
		final String deleteResult = msghandle.deleteMessages(msgIds); // OK or FAIL
		if (deleteResult == null || deleteResult.equals(RESPONSE_FAIL)) {
			throw new KokuActionProcessException("Deleting one or more messages failed.");
		}		
	}

	@Override
	public void archiveMessages(String[] messageIds)	throws KokuActionProcessException {
		
		if (messageIds == null) {
			throw new KokuActionProcessException("Archiving message(s) failed. messageIds parameter is null");
		}

		if (msghandle == null) {
			msghandle = new MessageHandle();
		}		
	
		List<Long> formattedMsgIds = new ArrayList<Long>();		
		for(String msgId : messageIds) {
			try {
				formattedMsgIds.add(Long.parseLong(msgId));				
			} catch (NumberFormatException nfe) {
				throw new KokuActionProcessException("Error while parsing messageIds. MessageId is not valid number: '"+msgId+"'");
			}
		}
		
		final String result = msghandle.archiveMessages(formattedMsgIds); // OK or FAIL
		if (result == null || result.equals(RESPONSE_FAIL)) {
			throw new KokuActionProcessException("Archiving one or more messages failed.");
		}
	}

	protected final String getUserId() {
		return userId;
	}
}
