package fi.arcusys.koku.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import fi.arcusys.koku.kv.messageservice.FolderType;

/**
 * Utilities used in the message portlet
 * @author Jinhua Chen
 * Aug 4, 2011
 */
public class MessageUtil {

	private MessageUtil() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
	
	public static final String DATE_FORMAT = "d.M.yyyy HH:mm:ss";
	public static final String TIME_ZONE = "Europe/Helsinki";
	public static final int PAGE_NUMBER = 10; // number of tasks in one page
	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_FAIL = "FAIL";

	/**
	 * Gets the message folder type converting from message type
	 * @param messageType
	 * @return FolderType, default is FolderType.INBOX
	 */
	public static FolderType getFolderType(String messageType) {
		
		if(messageType.equals("msg_inbox")) {
			return FolderType.INBOX;
		}else if(messageType.equals("msg_outbox")) {
			return FolderType.OUTBOX;
		}else if(messageType.equals("msg_archive_inbox")) {
			return FolderType.ARCHIVE_INBOX;
		}else if(messageType.equals("msg_archive_outbox")) {
			return FolderType.ARCHIVE_OUTBOX;
		}else {
			return FolderType.INBOX;
		}
	}
	
	/**
	 * Gets the message type converting from folder type
	 * @param folderType
	 * @return Message type, default is inbox
	 */
	public static String getMessageType(FolderType folderType) {
		
		if(folderType.equals(FolderType.INBOX)) {
			return "inbox";
		}else if(folderType.equals(FolderType.OUTBOX)) {
			return "outbox";
		}else if(folderType.equals(FolderType.ARCHIVE_INBOX)) {
			return "archive_inbox";
		}else if(folderType.equals(FolderType.ARCHIVE_OUTBOX)) {
			return "archive_outbox";
		}else {
			return "inbox";
		}
	}

	/**
	 * Formats the date with given format and Helsinki timezone
	 * @param xmlGregorianCalendar
	 * @return formatted date string
	 */	
	public static String formatTaskDate(XMLGregorianCalendar xmlGregorianCalendar) {
		
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
	 * Formats the date with given format with given format string
	 * @param xmlGregorianCalendar
	 * @param formatString
	 * @return formatted date string
	 */
	public static String formatDateByString(XMLGregorianCalendar xmlGregorianCalendar, String formatString) {
		
		if(xmlGregorianCalendar != null ) {
			Calendar cal = xmlGregorianCalendar.toGregorianCalendar();
			SimpleDateFormat dataformat = new SimpleDateFormat(formatString);
			//dataformat.setTimeZone(TimeZone.getTimeZone(MessageUtil.TIME_ZONE));
			String timeStr = dataformat.format(cal.getTime());
		
			return timeStr;	
		} else {
			return "";
		}
	}
}

