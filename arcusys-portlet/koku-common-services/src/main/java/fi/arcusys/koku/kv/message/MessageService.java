package fi.arcusys.koku.kv.message;

import static fi.arcusys.koku.util.Constants.RESPONSE_FAIL;
import static fi.arcusys.koku.util.Constants.RESPONSE_OK;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.messageservice.Criteria;
import fi.arcusys.koku.kv.messageservice.FolderType;
import fi.arcusys.koku.kv.messageservice.KokuMessageService_Service;
import fi.arcusys.koku.kv.messageservice.MessageQuery;
import fi.arcusys.koku.kv.messageservice.MessageStatus;
import fi.arcusys.koku.kv.messageservice.MessageSummary;
import fi.koku.settings.KoKuPropertiesUtil;


/**
 * Retrieves messages data and related operations via web services
 * @author Jinhua Chen
 * Jul 29, 2011
 */
public class MessageService {
	
	private static final URL MESSAGE_WSDL_LOCATION;
	private KokuMessageService_Service ms;
	
	private static final Logger LOG = Logger.getLogger(MessageService.class);		
	
	static {
		try {
			LOG.info("KvMessageservice WSDL location: "+ KoKuPropertiesUtil.get("KvMessageService"));
			MESSAGE_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("KvMessageService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create KvMessageservice WSDL url! Given URL address is not valid!");
			throw new ExceptionInInitializerError(e);
		}
	}
	
	/**
	 * Constructor and initialization
	 */
	public MessageService() {
		this.ms = new KokuMessageService_Service(MESSAGE_WSDL_LOCATION);
	}

	/**
	 * Gets messages
	 * @param user username
	 * @param folderType Message folder type such as inbox, outbox and archive
	 * @param messageQuery query for filtering messages
	 * @return a list of messages
	 */
	public List<MessageSummary> getMessages(String user, FolderType folderType, MessageQuery messageQuery) throws KokuServiceException {
		try {
			return ms.getKokuMessageServicePort().getMessages(user, folderType, messageQuery);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getMessages failed. user: '"+user+"' folderType: '"+folderType+"'", e);
		}
	}
	
	/**
	 * Gets total number of messages
	 * @param user username
	 * @param folderType  Message folder type such as inbox, outbox and archive
	 * @param criteria criteria for filtering messages
	 * @return the amount of messages
	 */
	public int getTotalMessageNum(String user, FolderType folderType, Criteria criteria) throws KokuServiceException {
		try {
			return ms.getKokuMessageServicePort().getTotalMessages(user, folderType, criteria);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalMessageNum failed. user: '"+user+"' folderType: '"+folderType+"'", e);
		}
	}

	/**
	 * @deprecated
	 * Gets the list of message summary.
	 * @param user Username
	 * @param FolderType Message folder type such as inbox, outbox and archive
	 * @param subQuery Basic query for the message, such as 'message_subject like %keyword%' , 'ORDER BY message_creationDate'
	 * @param startNum The start message number that fulfills the condition
	 * @param maxNum The maximum amount of messages that fulfills the condition
	 * @return List of messages
	 */
	public List<MessageSummary> getMessagesOld(String user, FolderType folderType, String subQuery, int startNum, int maxNum) throws KokuServiceException {
		try {
			return ms.getKokuMessageServicePort().getMessagesOld(user, folderType, subQuery, startNum, maxNum);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getMessagesOld failed. user: '"+user+"' folderType: '"+folderType+"'", e);
		}
	}
	
	/**
	 * Gets the detailed message with content by messageId
	 * @param messageId
	 * @return The detailed message
	 */
	public fi.arcusys.koku.kv.messageservice.Message getMessageById(long messageId) throws KokuServiceException {
		try {
			return ms.getKokuMessageServicePort().getMessageById(messageId);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getMessageById failed. messageId: '"+messageId+"'", e);
		}
	}
	
	/**
	 * @deprecated
	 * Gets the total message number
	 * @param user Username
	 * @param FolderType Message folder type such as inbox, outbox and archive
	 * @param subQuery Basic query for the message, such as 'message_subject like %keyword%' , 'ORDER BY message_creationDate'
	 * @return The number of messages
	 */
	public int getTotalMessageNumOld(String user, FolderType folderType, String subQuery) throws KokuServiceException {
		try {
			return ms.getKokuMessageServicePort().getTotalMessagesOld(user, folderType, subQuery);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getTotalMessageNumOld failed. messageId: user: '"+user+"' folderType: '"+folderType+"' subQuery: '"+subQuery+"'", e);
		}
	}
	
	/**
	 * Gets unread messages
	 * @param user Username
	 * @param FolderType Message folder type such as inbox, outbox and archive
	 * @return Unread messages
	 */
	public int getUnreadMessageNum(String user, FolderType folderType) throws KokuServiceException {
		try {
			return ms.getKokuMessageServicePort().getUnreadMessages(user, folderType);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUnreadMessageNum failed. messageId: user: '"+user+"' folderType: '"+folderType+"'", e);
		}
	}
	
	/**
	 * Deletes the messages
	 * @param messageIds List of message ids to be deleted
	 * @return Operation status
	 */
	public String deleteMessages(List<Long> messageIds) {
				
		try {
			ms.getKokuMessageServicePort().deleteMessages(messageIds);
			return RESPONSE_OK;
		} catch(RuntimeException e) {
			LOG.error("ERROR while deleting message(s). See errorMsg: "+ e.getMessage(), e);
			return RESPONSE_FAIL;
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
			return RESPONSE_OK;
		} catch(RuntimeException e) {
			LOG.error("ERROR while setting message(s) status. See errorMsg: "+ e.getMessage(), e);
			return RESPONSE_FAIL;
		}
		
	}
	
	/**
	 * Archives messages
	 * 
	 * @param messageIds a list of message ids
	 * @return Operation status
	 */
	public String archiveMessages(List<Long> messageIds){
		try {
			ms.getKokuMessageServicePort().archiveMessages(messageIds);
			return RESPONSE_OK;
		} catch(RuntimeException e) {
			LOG.error("ERROR while archiveMessages. See errorMsg: "+ e.getMessage(), e);
			return RESPONSE_FAIL;
		}
	}
	
	/**
	 * Archives old messages (more than 3 months older)
	 * 
	 * @param userUid
	 * @param folderType
	 */
	public String archiveOldMessages(String userUid, FolderType folderType) {
		try {
			ms.getKokuMessageServicePort().archiveOldMessages(userUid, folderType);
			return RESPONSE_OK;
		} catch (RuntimeException e) {
			LOG.error("ERROR while archiveOldMessages. See errorMsg: "+ e.getMessage(), e);
			return RESPONSE_FAIL;
		}
	}
	
}
