package fi.arcusys.koku.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import fi.arcusys.koku.service.FolderType;
import fi.arcusys.koku.service.MessageStatus;
import fi.arcusys.koku.service.MessageSummary;
import fi.arcusys.koku.util.MessageUtil;

public class MessageHandle {
	
	public MessageHandle() {}
	
	/**
	 * Gets user summary messages without detailed content, and converts to the message model for web interface
	 * @param user
	 * @param messageType
	 * @param keyword
	 * @param orderType
	 * @param start
	 * @param max
	 * @return
	 */
	public List<Message> getMessages(String user, String messageType, String keyword, String orderType, int start, int max) {
		MessageService ms = new MessageService();
		FolderType folderType = MessageUtil.getFolderType(messageType);
		String subQuery = "";
		
		List<MessageSummary> msgs;		
		msgs = ms.getMessages(user, folderType, subQuery, start, max);
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
			message.setCreationDate(formatTaskDate(msgSum.getCreationDate()));
			message.setMessageType(MessageUtil.getMessageType(msgSum.getMessageType()));
			message.setMessageStatus(msgSum.getMessageStatus().toString().toLowerCase());
			
			msgList.add(message);
		}
		
		return msgList;
	}
	
	/**
	 * Gets detailed message with content
	 * @param messageId
	 * @return
	 */
	public Message getMessageById(String messageId) {
		MessageService ms = new MessageService();		
		long  msgId = (long) Long.parseLong(messageId);
		setMessageStatus(msgId);
		
		fi.arcusys.koku.service.Message msg = ms.getMessageById(msgId);
		Message message = new Message();
		message.setMessageId(msg.getMessageId());
		message.setSender(msg.getSender());
		message.SetRecipients(formatRecipients(msg.getRecipients()));
		message.setSubject(msg.getSubject());
		message.setContent(msg.getContent());
		message.setCreationDate(formatTaskDate(msg.getCreationDate()));
		message.setMessageStatus(msg.getMessageStatus().toString().toLowerCase());	
		
		return message;
	}
	
	/**
	 * Sets message status to read
	 * @param messageId
	 * @return
	 */
	public void setMessageStatus(long messageId) {
		MessageService ms = new MessageService();
		List<Long> messageIds = new ArrayList<Long>();
		
		messageIds.add(messageId);
		
		ms.setMessageStatus(messageIds, MessageStatus.READ);
	}
	
	/**
	 * Archives messages
	 * @param messageIds
	 * @return
	 */
	public String archiveMessages(List<Long> messageIds) {		
		MessageService ms = new MessageService();
		
		return ms.archiveMessages(messageIds);		
	}
	
	/**
	 * Deletes messages
	 * @param messageIds
	 * @return
	 */
	public String deleteMessages(List<Long> messageIds) {		
		MessageService ms = new MessageService();
		
		return ms.deleteMessages(messageIds);		
	}
	
	/**
	 * Gets the amount of messages
	 * @param user
	 * @param messageType
	 * @param keyword
	 * @param orderType
	 * @return
	 */
	public int getTotalMessageNum(String user, String messageType, String keyword, String orderType) {
		MessageService ms = new MessageService();
		FolderType folderType = MessageUtil.getFolderType(messageType);
		String subQuery = "";
		
		return ms.getTotalMessageNum(user, folderType, subQuery);		
	}
	/**
	 * Format the task date with given format and Helsinki timezone
	 * @param xmlGregorianCalendar
	 * @return formatted date string
	 */
	public String formatTaskDate(XMLGregorianCalendar xmlGregorianCalendar) {
		
		if(xmlGregorianCalendar != null ) {
			Calendar cal = xmlGregorianCalendar.toGregorianCalendar();
			SimpleDateFormat dataformat = new SimpleDateFormat(MessageUtil.DATE_FORMAT);
			dataformat.setTimeZone(TimeZone.getTimeZone(MessageUtil.TIME_ZONE));
			String dateStr = dataformat.format(cal.getTime());
		
			return dateStr;	
		} else {
			return "";
		}
	}
	
	/**
	 * Formats the recipients list by presenting with string
	 * @param recipients
	 * @return
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

}
