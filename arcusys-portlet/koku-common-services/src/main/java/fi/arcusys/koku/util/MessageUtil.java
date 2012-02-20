package fi.arcusys.koku.util;

import static fi.arcusys.koku.util.Constants.DATE;
import static fi.arcusys.koku.util.Constants.DATE_FORMAT;
import static fi.arcusys.koku.util.Constants.TIME_ZONE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import fi.arcusys.koku.kv.messageservice.FolderType;

/**
 * Utilities used in the message portlet
 * @author Jinhua Chen
 * Aug 4, 2011
 */
public final class MessageUtil {

	private MessageUtil() {
        // prevents calls from subclass
        throw new UnsupportedOperationException();
    }
	
	private static final Locale locale = new Locale("fi", "FI");
    private static DatatypeFactory datatypeFactory = null;

    static {
        try {
        	datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException("Cannot instantiate DatatypeFactory!", dce);
        }
    }  
	
	/**
	 * Returns default locale
	 * 
	 * FIXME: For now this will return only Finnish locale.
	 * 
	 * @return Locale
	 */
	public static Locale getLocale() {
		return locale;
	}

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
			SimpleDateFormat dateformat = new SimpleDateFormat(DATE_FORMAT);
			dateformat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
			String dateStr = dateformat.format(cal.getTime());		
			return dateStr;	
		} else {
			return "";
		}
	}
	
	/**
	 * Formats the date in day
	 * @param xmlGregorianCalendar
	 * @return formatted date string
	 */
	public static String formatTaskDateByDay(XMLGregorianCalendar xmlGregorianCalendar) {				
		return formatDateByString(xmlGregorianCalendar, DATE);
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
			SimpleDateFormat dateformat = new SimpleDateFormat(formatString);	
//			dateformat.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
			dateformat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
			String timeStr = dateformat.format(cal.getTime());
		
			return timeStr;	
		} else {
			return "";
		}
	}
		
	/**
	 * Formats a list of string to string
	 * @param recipients a list of string
	 * @return recipients string
	 */
	public static String formatRecipients(List<String> recipients) {
	    final StringBuilder result = new StringBuilder();
	    
	    for (final String recipient : recipients) {
	        final String user = recipient.trim();
            if (!user.isEmpty()) {
                result.append(user).append(", ");
            }
	    }
	    
	    if (result.length() > 0) {
	        result.setLength(result.length() - 2);
	    }
	    
	    return result.toString();
	}
	
	

    /**
     * Converts Date object to XMLGregorianCalendar
     *
     * @param date 
     * @return {@link XMLGregorianCalendar}
     */
    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return datatypeFactory.newXMLGregorianCalendar(gc);
        }
    }	
	
}

