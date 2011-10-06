package fi.arcusys.koku.kv;


public enum KokuFolderType {
	
	    INBOX("Inbox"),
	    OUTBOX("Outbox"),
	    ARCHIVE_INBOX("Archive_Inbox"),
	    ARCHIVE_OUTBOX("Archive_Outbox");
	    
	    private final String value;

	    KokuFolderType(String v) {
	        value = v;
	    }

	    public String value() {
	        return value;
	    }

	    public static KokuFolderType fromValue(String v) {
	        for (KokuFolderType c: KokuFolderType.values()) {
	            if (c.toString().equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
}
