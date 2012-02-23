package fi.arcusys.koku.kv.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.context.NoSuchMessageException;

import fi.arcusys.koku.AbstractHandle;
import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.kv.messageservice.Criteria;
import fi.arcusys.koku.kv.messageservice.Fields;
import fi.arcusys.koku.kv.messageservice.FolderType;
import fi.arcusys.koku.kv.messageservice.MessageQuery;
import fi.arcusys.koku.kv.messageservice.MessageStatus;
import fi.arcusys.koku.kv.messageservice.MessageSummary;
import fi.arcusys.koku.kv.messageservice.OrderBy;
import fi.arcusys.koku.kv.messageservice.Type;
import fi.arcusys.koku.kv.messageservice.User;
import fi.arcusys.koku.kv.model.KokuFolderType;
import fi.arcusys.koku.kv.model.KokuMessage;
import fi.arcusys.koku.kv.messageservice.Message;
import fi.arcusys.koku.users.KokuUser;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Handles request of messages and retrieves messages
 * @author Jinhua Chen
 * Jun 22, 2011
 */
public class MessageHandle extends AbstractHandle {
	
	private static final Logger LOG = Logger.getLogger(MessageHandle.class);
	
	MessageService ms;
	
	public MessageHandle() {
		ms = new MessageService();
	}
	
	public int getUnreadMessages(String username, KokuFolderType folderType) throws KokuServiceException {
		FolderType type = FolderType.valueOf(folderType.toString());
		return ms.getUnreadMessageNum(username, type);
	}	
	
	/**
	 * Gets messages with filtering conditions
	 * 
	 * @param user user name
	 * @param messageType type of messages
	 * @param keyword keyword string for filtering
	 * @param field field string for filtering
	 * @param orderType type of message order
	 * @param start start index of message
	 * @param max maximum amount of messages
	 * @return a list of messages
	 */
	public List<KokuMessage> getMessages(String userId, String messageType, String keyword, String field, String orderType, int start, int max) throws KokuServiceException {
		FolderType folderType = MessageUtil.getFolderType(messageType);
		MessageQuery messageQuery = new MessageQuery();
		messageQuery.setStartNum(start);
		messageQuery.setMaxNum(max);
		
		/* sets the criteria for searching including keyword for each field, default is searching all fields */
		Criteria criteria = createCriteria(keyword, field);				
		messageQuery.setCriteria(criteria);
		
		/* sets the order type, default is ordering by created date descending */
		OrderBy orderby = new OrderBy();
		orderby.setField(Fields.CREATED_DATE);
		orderby.setType(Type.DESC);		
		messageQuery.getOrderBy().add(orderby);
		
		List<MessageSummary> msgs;		
		msgs = ms.getMessages(userId, folderType, messageQuery);
		List<KokuMessage> msgList = new ArrayList<KokuMessage>();
		KokuMessage message;		
		Iterator<MessageSummary> it = msgs.iterator();
		
		while(it.hasNext()) {
			MessageSummary msgSum = it.next();
			message = new KokuMessage();
			convertMessageSummaryToKokuMessage(message, msgSum);
			msgList.add(message);
		}		
		return msgList;
	}
	
	/**
	 * Gets the total amount of messages with given filtering conditions
	 * 
	 * @param user username
	 * @param messageType type of message
	 * @param keyword keyword string for filtering
	 * @param field field string for filtering
	 * @return number of messages
	 */
	public int getTotalMessageNum(String userId, String messageType, String keyword, String field) throws KokuServiceException {

		FolderType folderType = MessageUtil.getFolderType(messageType);
		/* sets the criteria for searching including keyword for each field, default is searching all fields */
		Criteria criteria = createCriteria(keyword, field);
		return ms.getTotalMessageNum(userId, folderType, criteria);		
	}
	
	/**
	 * @deprecated
	 * Gets user summary messages
	 * 
	 * @param user username 
	 * @param messageType type of message
	 * @param keyword keyword for filtering
	 * @param orderType type of message order
	 * @param start start index of message
	 * @param max maximum amount of messages
	 * @return a list of messages
	 */
	public List<KokuMessage> getMessagesOld(String userId, String messageType, String keyword, String orderType, int start, int max) throws KokuServiceException {

		FolderType folderType = MessageUtil.getFolderType(messageType);
		String subQuery = "";
		
		List<MessageSummary> msgs;		
		msgs = ms.getMessagesOld(userId, folderType, subQuery, start, max);
		List<KokuMessage> msgList = new ArrayList<KokuMessage>();
		KokuMessage message;		
		Iterator<MessageSummary> it = msgs.iterator();
		
		while(it.hasNext()) {
			MessageSummary msgSum = it.next();
			message = new KokuMessage();
			convertMessageSummaryToKokuMessage(message, msgSum);
			msgList.add(message);
		}
		
		return msgList;
	}
	
	/**
	 * Gets detailed message with content
	 * 
	 * @param messageId message id
	 * @return detailed message
	 */
	public KokuMessage getMessageById(String messageId) throws KokuServiceException {
		long  msgId = 0;
		try {
			 msgId = (long) Long.parseLong(messageId);			
		} catch (NumberFormatException nfe) {
			LOG.error("Couldn't show message details, because messageId can't parse properly (must be long). Given MessageId: "+messageId, nfe);
			return new KokuMessage();
		}
		setMessageStatusRead(msgId);		
		Message msg = ms.getMessageById(msgId);
		KokuMessage message = new KokuMessage();
		convertMessageToKokuMessage(message, msg);
		return message;
	}
	
	private void convertMessageSummaryToKokuMessage(KokuMessage kokuMessage, MessageSummary message) {
		kokuMessage.setMessageId(message.getMessageId());
		kokuMessage.setSubject(message.getSubject());
		kokuMessage.setCreationDate(MessageUtil.formatTaskDate(message.getCreationDate()));
		kokuMessage.setMessageType(MessageUtil.getMessageType(message.getMessageType()));
		kokuMessage.setMessageStatus(message.getMessageStatus().value());
		kokuMessage.setMessageStatusLocalized(localizeMsgStatus(message.getMessageStatus()));
		for (User recipientUser : message.getRecipientUserInfos()) {
			kokuMessage.getRecipientUsers().add(new KokuUser(recipientUser));
		}
		kokuMessage.setSenderUser(new KokuUser(message.getSenderUserInfo()));
	}
	
	private void convertMessageToKokuMessage(KokuMessage kokuMessge, Message message) {
		convertMessageSummaryToKokuMessage(kokuMessge, message);
		if (message.getDeliveryFailedTo() != null) {
			for (User user : message.getDeliveryFailedTo()) {
				kokuMessge.getDeliveryFailedTo().add(new KokuUser(user));				
			}
		}
		kokuMessge.setContent(message.getContent());
	}
	
	
	private String localizeMsgStatus(MessageStatus status ) {
		if (getMessageSource() == null) {
			LOG.warn("getMessageSource() is null. Localization doesn't work properly");
			return status.toString().toLowerCase();
		}
		Locale locale = MessageUtil.getLocale();
		
		try {	
			switch(status) {
			case READ:
				return getMessageSource().getMessage("MessageStatus.READ", null, locale);
			case UNREAD:
				return getMessageSource().getMessage("MessageStatus.UNREAD", null, locale);
			default:
				return getMessageSource().getMessage("unknown", null, locale);
			}
		} catch (NoSuchMessageException nsme) {
			LOG.warn("Coulnd't find localized message. Localization doesn't work properly");
			return status.toString().toLowerCase();
		}
	}
	
	/**
	 * Sets message status to read
	 * 
	 * @param messageId message id
	 */
	private void setMessageStatusRead(long messageId) {
		List<Long> messageIds = new ArrayList<Long>();	
		messageIds.add(messageId);	
		ms.setMessageStatus(messageIds, MessageStatus.READ);
	}
	
	/**
	 * Archives messages
	 * 
	 * @param messageIds a list of message ids
	 * @return operation response information
	 */
	public String archiveMessages(List<Long> messageIds) {		
		return ms.archiveMessages(messageIds);		
	}
	
	/**
	 * Archives old messages
	 * 
	 * @param userUid
	 * @param folderType
	 * @return operation response information
	 */
	public String archiveOldMessages(String userUid, FolderType folderType) {
		return ms.archiveOldMessages(userUid, folderType);
	}
	
	/**
	 * Deletes messages
	 * 
	 * @param messageIds a list of message ids
	 * @return operation response information
	 */
	public String deleteMessages(List<Long> messageIds) {				
		return ms.deleteMessages(messageIds);		
	}
	
	/**
	 * @deprecated
	 * Gets the amount of messages
	 * 
	 * @param user username
	 * @param messageType type of message
	 * @param keyword keyword string for filtering
	 * @param orderType type of message order
	 * @return the amount of messages
	 */
	public int getTotalMessageNumOld(String user, String messageType, String keyword, String orderType) throws KokuServiceException {
		FolderType folderType = MessageUtil.getFolderType(messageType);
		String subQuery = "";
		
		return ms.getTotalMessageNumOld(user, folderType, subQuery);		
	}
	
	/**
	 * Formats the recipients list to be presented with string
	 * @param recipients a list of recipients of message
	 * @return recipient string to be presented
	 */
//	public String formatRecipients(List<String> recipients) {
//		Iterator<String> it = recipients.iterator();
//		String recipientStr = "";
//		String recipient;
//		
//		while(it.hasNext()) {
//			recipient = it.next();
//			
//			if(recipient.trim().length() > 0) {
//				recipientStr += recipient + ", ";
//			}
//		}
//		
//		if(recipientStr.lastIndexOf(",") > 0) {
//			recipientStr = recipientStr.substring(0, recipientStr.length()-2);
//		}
//		
//		return recipientStr;
//	}
	
	/**
	 * Creates filtering criteria
	 * 
	 * @param keyword keyword string
	 * @param field filed string
	 * @return filtering criteria
	 */
	public Criteria createCriteria(String keyword, String field) {
		Criteria criteria = new Criteria();
		
		if(keyword.trim().length() > 0) {
			String[] keywords = keyword.split(" ");
			criteria.getKeywords().addAll(Arrays.asList(keywords));
		}else {
			return null;
		}
		
		String[] fields = field.split("_");
		
		for(int i=0; i < fields.length; i++) {
			if(fields[i].equals("1")) {
				criteria.getFields().add(Fields.SENDER);
			}else if(fields[i].equals("2")) {
				criteria.getFields().add(Fields.RECEIVER);
			}else if(fields[i].equals("3")) {
				criteria.getFields().add(Fields.SUBJECT);
			}else if(fields[i].equals("4")) {
				criteria.getFields().add(Fields.CONTENT);
			}
		}
		return criteria;
	}
}
