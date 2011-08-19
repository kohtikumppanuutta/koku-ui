package fi.arcusys.koku.message;

import java.net.URL;
import java.util.List;

import fi.arcusys.koku.messageservice.Criteria;
import fi.arcusys.koku.messageservice.FolderType;
import fi.arcusys.koku.messageservice.KokuMessageService_Service;
import fi.arcusys.koku.messageservice.MessageQuery;
import fi.arcusys.koku.messageservice.MessageStatus;
import fi.arcusys.koku.messageservice.MessageSummary;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Retrieves messages data from message web services
 * @author Jinhua Chen
 * Jul 29, 2011
 */
public class MessageService {
	
	private final URL MESSAGE_WSDL_LOCATION = getClass().getClassLoader().getResource("MessageService.wsdl");
	private KokuMessageService_Service ms;
	
	/**
	 * Constructor
	 */
	public MessageService() {
		this.ms = new KokuMessageService_Service(MESSAGE_WSDL_LOCATION);
	}

	/**
	 * 
	 * @param user username
	 * @param folderType Message folder type such as inbox, outbox and archive
	 * @param messageQuery query for filtering messages
	 * @return a list of messages
	 */
	public List<MessageSummary> getMessages(String user, FolderType folderType, MessageQuery messageQuery) {

		return ms.getKokuMessageServicePort().getMessages(user, folderType, messageQuery);
	}
	
	/**
	 * 
	 * @param user username
	 * @param folderType  Message folder type such as inbox, outbox and archive
	 * @param criteria criteria for filtering messages
	 * @return the amount of messages
	 */
	public int getTotalMessageNum(String user, FolderType folderType, Criteria criteria) {
		
		return ms.getKokuMessageServicePort().getTotalMessages(user, folderType, criteria);
	}

	/**
	 * @deprecated
	 * Gets the list of message summary.
	 * @param user Username
	 * @param FolderType Message folder type such as inbox, outbox and archive
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
	public fi.arcusys.koku.messageservice.Message getMessageById(long messageId) {
		
		return ms.getKokuMessageServicePort().getMessageById(messageId);
	}
	
	/**
	 * @deprecated
	 * Gets the total message number
	 * @param user Username
	 * @param FolderType Message folder type such as inbox, outbox and archive
	 * @param subQuery Basic query for the message, such as 'message_subject like %keyword%' , 'ORDER BY message_creationDate'
	 * @return The number of messages
	 */
	public int getTotalMessageNumOld(String user, FolderType folderType, String subQuery) {
		
		return ms.getKokuMessageServicePort().getTotalMessagesOld(user, folderType, subQuery);
	}
	
	/**
	 * Gets unread messages
	 * @param user Username
	 * @param FolderType Message folder type such as inbox, outbox and archive
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
	 * @param messageStatus message status
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
	 * @param messageIds a list of message ids
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
