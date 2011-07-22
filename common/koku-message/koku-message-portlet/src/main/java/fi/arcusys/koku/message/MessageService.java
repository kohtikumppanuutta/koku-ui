package fi.arcusys.koku.message;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.service.Criteria;
import fi.arcusys.koku.service.FolderType;
import fi.arcusys.koku.service.KokuMessageService_Service;
import fi.arcusys.koku.service.Message;
import fi.arcusys.koku.service.MessageQuery;
import fi.arcusys.koku.service.MessageStatus;
import fi.arcusys.koku.service.MessageSummary;
import fi.arcusys.koku.util.MessageUtil;


public class MessageService {
	
	public final URL MESSAGE_WSDL_LOCATION = getClass().getClassLoader().getResource("MessageService.wsdl");
	private KokuMessageService_Service ms;
	
	public MessageService() {
		this.ms = new KokuMessageService_Service(MESSAGE_WSDL_LOCATION);
	}

	public List<MessageSummary> getMessages(String user, FolderType folderType, MessageQuery messageQuery) {

		return ms.getKokuMessageServicePort().getMessages(user, folderType, messageQuery);
	}
	
	public int getTotalMessageNum(String user, FolderType folderType, Criteria criteria) {
		
		return ms.getKokuMessageServicePort().getTotalMessages(user, folderType, criteria);
	}

	/**
	 * Gets the list of message summary.
	 * @param user Username
	 * @param messageType Message Type such as inbox, outbox and archive
	 * @param subQuery Basic query for the message, such as 'message_subject like %keyword%' , 'ORDER BY message_creationDate'
	 * @param startNum The start message number that fulfill the condition
	 * @param maxNum The maximum amount of messages that fulfill the condition
	 * @return List of messages
	 */
	public List<MessageSummary> getMessagesOld(String user, FolderType folderType, String subQuery, int startNum, int maxNum) {
		
		return ms.getKokuMessageServicePort().getMessagesOld(user, folderType, subQuery, startNum, maxNum);
	}
	
	/**
	 * Gets the detailed message with content by messageId
	 * @param messageId
	 * @return The detailed message
	 */
	public Message getMessageById(long messageId) {
		
		return ms.getKokuMessageServicePort().getMessageById(messageId);
	}
	
	/**
	 * Gets the total message number
	 * @param user Username
	 * @param messageType Message Type such as inbox, outbox and archive
	 * @param subQuery Basic query for the message, such as 'message_subject like %keyword%' , 'ORDER BY message_creationDate'
	 * @return The number of messages
	 */
	public int getTotalMessageNumOld(String user, FolderType folderType, String subQuery) {
		
		return ms.getKokuMessageServicePort().getTotalMessagesOld(user, folderType, subQuery);
	}
	
	/**
	 * Gets unread messages
	 * @param user Username
	 * @param messageType Message Type such as inbox, outbox and archive
	 * @return Unread messages
	 */
	public int getUnreadMessageNum(String user, FolderType folderType) {
		
		return ms.getKokuMessageServicePort().getUnreadMessages(user, folderType);
	}
	
	/**
	 * Deletes the messages
	 * @param messageIds List of message ids to be deleted
	 * @return Operation status
	 */
	public String deleteMessages(List<Long> messageIds) {
				
		try {
			ms.getKokuMessageServicePort().deleteMessages(messageIds);
			return MessageUtil.RESPONSE_OK;
		} catch(RuntimeException e) {
			return MessageUtil.RESPONSE_FAIL;
		}
	}
	
	/**
	 * Sets the message status: read or unread
	 * @param messageIds List of message ids
	 * @param messageStatus
	 * @return Operation status
	 */
	public String setMessageStatus(List<Long> messageIds, MessageStatus messageStatus) {
		
		try {
			ms.getKokuMessageServicePort().setMessagesStatus(messageIds, messageStatus);
			return MessageUtil.RESPONSE_OK;
		} catch(RuntimeException e) {
			return MessageUtil.RESPONSE_FAIL;
		}
		
	}
	
	/**
	 * Archives messages
	 * @param messageIds
	 * @return Operation status
	 */
	public String archiveMessages(List<Long> messageIds){
		
		try {
			ms.getKokuMessageServicePort().archiveMessages(messageIds);
			return MessageUtil.RESPONSE_OK;
		} catch(RuntimeException e) {
			return MessageUtil.RESPONSE_FAIL;
		}
		
	}
}
