package fi.arcusys.koku.util;

public enum KokuWebServicesJS {
	
	APPOINTMENT_PROCESSING_SERVICE("KokuAppointmentProcessingService"),	
	SUOSTUMUS_PROCESSING_SERVICE("KokuSuostumusProcessingService"),
	VALTAKIRJA_PROCESSING_SERVICE("KokuValtakirjaProcessingService"),
	TIETOPYYNTO_PROCESSING_SERVICE("KokuTietopyyntoProcessingService"),
	HAK_PROCESSING_SERVICE("KokuHakProcessingService"),
	REQUEST_PROCESSING_SERVICE("KokuRequestProcessingService"),
	USERS_AND_GROUPS_SERVICE("UsersAndGroupsService");
	
	private final String value;
	
	KokuWebServicesJS(String v) {
	    value = v;
	}
	
	public String value() {
	    return value;
	}
	
	public static KokuWebServicesJS fromValue(String v) {
	    for (KokuWebServicesJS c: KokuWebServicesJS.values()) {
	        if (c.toString().equals(v)) {
	            return c;
	        }
	    }
	    throw new IllegalArgumentException(v);
	}	
}
