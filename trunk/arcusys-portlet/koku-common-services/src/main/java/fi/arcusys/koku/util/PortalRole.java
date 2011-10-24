package fi.arcusys.koku.util;

/**
 * 
 * PortalRole
 * 
 * This setting describes service which userId resolving method it should use. 
 * 
 * @author Toni Turunen
 *
 */
public enum PortalRole {
	
	CITIZEN("Citizen"),
	EMPLOYEE("Employee");
	
	private final String value;
	
	PortalRole(String value) {
		this.value = value;
	}
	
    public String value() {
	    return value;
	}

	public static PortalRole fromValue(String v) {
	    for (PortalRole c: PortalRole.values()) {
	        if (c.toString().equals(v)) {
	            return c;
	        }
	    }
	    throw new IllegalArgumentException(v);
	}
}
