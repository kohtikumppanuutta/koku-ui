package fi.arcusys.koku.util;

import fi.arcusys.koku.service.FolderType;

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
		
		if(messageType.equals("inbox")) {
			return FolderType.INBOX;
		}else if(messageType.equals("outbox")) {
			return FolderType.OUTBOX;
		}else if(messageType.equals("archive_inbox")) {
			return FolderType.ARCHIVE_INBOX;
		}else if(messageType.equals("archive_outbox")) {
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

}

