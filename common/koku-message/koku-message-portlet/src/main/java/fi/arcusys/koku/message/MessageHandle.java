package fi.arcusys.koku.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fi.arcusys.koku.messageservice.Criteria;
import fi.arcusys.koku.messageservice.Fields;
import fi.arcusys.koku.messageservice.FolderType;
import fi.arcusys.koku.messageservice.MessageQuery;
import fi.arcusys.koku.messageservice.MessageStatus;
import fi.arcusys.koku.messageservice.MessageSummary;
import fi.arcusys.koku.messageservice.OrderBy;
import fi.arcusys.koku.messageservice.Type;
import fi.arcusys.koku.util.MessageUtil;

/**
 * Handles request of messages and retrieves messages
 * @author Jinhua Chen
 * Jun 22, 2011
 */
public class MessageHandle {
	
	public MessageHandle() {}
	
	/**
	 * Gets messages with filtering conditions
	 * @param user username
	 * @param messageType type of messages
	 * @param keyword keyword string for filtering
	 * @param field field string for filtering
	 * @param orderType type of message order
	 * @param start start index of message
	 * @param max maximum amount of messages
	 * @return a list of messages
	 */
	public List<Message> getMessages(String user, String messageType, String keyword, String field, String orderType, int start, int max) {
		MessageService ms = new MessageService();
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
		msgs = ms.getMessages(user, folderType, messageQuery);
		List<Message> msgList = new ArrayList<Message>();
		Message message;		
		Iterator<MessageSummary> it = msgs.iterator();
		
		while(it.hasNext()) {
			MessageSummary msgSum = it.next();
			message = new Message();
			message.setMessageId(msgSum.getMessageId());
			message.setSender(msgSum.getSender());
			message.SetRecipients(formatRecipients(msgSum.getRecipients()));
			message.setSubject(msgSum.getSubject());
			message.setCreationDate(MessageUtil.formatTaskDate(msgSum.getCreationDate()));
			message.setMessageType(MessageUtil.getMessageType(msgSum.getMessageType()));
			message.setMessageStatus(msgSum.getMessageStatus().toString().toLowerCase());
			
			msgList.add(message);
		}
		
		return msgList;
	}
	
	/**
	 * Gets the total amount of messages with given filtering conditions
	 * @param user username
	 * @param messageType type of message
	 * @param keyword keyword string for filtering
	 * @param field field string for filtering
	 * @return number of messages
	 */
	public int getTotalMessageNum(String user, String messageType, String keyword, String field) {
		MessageService ms = new MessageService();
		FolderType folderType = MessageUtil.getFolderType(messageType);
		/* sets the criteria for searching including keyword for each field, default is searching all fields */
		Criteria criteria = createCriteria(keyword, field);		
		
		return ms.getTotalMessageNum(user, folderType, criteria);		
	}
	
	/**
	 * @deprecated
	 * Gets user summary messages
	 * @param user username 
	 * @param messageType type of message
	 * @param keyword keyword for filtering
	 * @param orderType type of message order
	 * @param start start index of message
	 * @param max maximum amount of messages
	 * @return a list of messages
	 */
	public List<Message> getMessagesOld(String user, String messageType, String keyword, String orderType, int start, int max) {
		MessageService ms = new MessageService();
		FolderType folderType = MessageUtil.getFolderType(messageType);
		String subQuery = "";
		
		List<MessageSummary> msgs;		
		msgs = ms.getMessagesOld(user, folderType, subQuery, start, max);
		List<Message> msgList = new ArrayList<Message>();
		Message message;		
		Iterator<MessageSummary> it = msgs.iterator();
		
		while(it.hasNext()) {
			MessageSummary msgSum = it.next();
			message = new Message();
			message.setMessageId(msgSum.getMessageId());
			message.setSender(msgSum.getSender());
			message.SetRecipients(formatRecipients(msgSum.getRecipients()));
			message.setSubject(msgSum.getSubject());
			message.setCreationDate(MessageUtil.formatTaskDate(msgSum.getCreationDate()));
			message.setMessageType(MessageUtil.getMessageType(msgSum.getMessageType()));
			message.setMessageStatus(msgSum.getMessageStatus().toString().toLowerCase());
			
			msgList.add(message);
		}
		
		return msgList;
	}
	
	/**
	 * Gets detailed message with content
	 * @param messageId message id
	 * @return detailed message
	 */
	public Message getMessageById(String messageId) {
		MessageService ms = new MessageService();		
		long  msgId = (long) Long.parseLong(messageId);
		setMessageStatus(msgId);
		
		fi.arcusys.koku.messageservice.Message msg = ms.getMessageById(msgId);
		Message message = new Message();
		message.setMessageId(msg.getMessageId());
		message.setSender(msg.getSender());
		message.SetRecipients(formatRecipients(msg.getRecipients()));
		message.setSubject(msg.getSubject());
		message.setContent(msg.getContent());
		message.setCreationDate(MessageUtil.formatTaskDate(msg.getCreationDate()));
		message.setMessageStatus(msg.getMessageStatus().toString().toLowerCase());	
		
		return message;
	}
	
	/**
	 * Sets message status to read
	 * @param messageId message id
	 */
	public void setMessageStatus(long messageId) {
		MessageService ms = new MessageService();
		List<Long> messageIds = new ArrayList<Long>();	
		messageIds.add(messageId);	
		ms.setMessageStatus(messageIds, MessageStatus.READ);
	}
	
	/**
	 * Archives messages
	 * @param messageIds a list of message ids
	 * @return operation response information
	 */
	public String archiveMessages(List<Long> messageIds) {		
		MessageService ms = new MessageService();
		
		return ms.archiveMessages(messageIds);		
	}
	
	/**
	 * Deletes messages
	 * @param messageIds a list of message ids
	 * @return operation response information
	 */
	public String deleteMessages(List<Long> messageIds) {		
		MessageService ms = new MessageService();
		
		return ms.deleteMessages(messageIds);		
	}
	
	/**
	 * @deprecated
	 * Gets the amount of messages
	 * @param user username
	 * @param messageType type of message
	 * @param keyword keyword string for filtering
	 * @param orderType type of message order
	 * @return the amount of messages
	 */
	public int getTotalMessageNumOld(String user, String messageType, String keyword, String orderType) {
		MessageService ms = new MessageService();
		FolderType folderType = MessageUtil.getFolderType(messageType);
		String subQuery = "";
		
		return ms.getTotalMessageNumOld(user, folderType, subQuery);		
	}
	
	/**
	 * Formats the recipients list to be presented with string
	 * @param recipients a list of recipients of message
	 * @return recipient string to be presented
	 */
	public String formatRecipients(List<String> recipients) {
		Iterator<String> it = recipients.iterator();
		String recipientStr = "";
		String recipient;
		
		while(it.hasNext()) {
			recipient = it.next();
			
			if(recipient.trim().length() > 0)
				recipientStr += recipient + ", ";		
		}
		
		if(recipientStr.lastIndexOf(",") > 0)
			recipientStr = recipientStr.substring(0, recipientStr.length()-2);
		
		return recipientStr;
	}
	
	/**
	 * Creates filtering criteria
	 * @param keyword
	 * @param field
	 * @return
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
