package fi.arcusys.koku.util;



/**
 * 
 * Arcusys portlets WebServices key names (for properties)
 * 
 * @author Toni Turunen
 *
 */
public enum KokuWebServices {
	
	INTALIO_TASKMGR_WSDL("TaskManagerService"),
	INTALIO_TOKEN_WSDL	("TokenService"),
	USERS_AND_GROUPS 	("UsersAndGroupsService"),
	TIVA_CITIZEN 		("TivaCitizenService"),
	TIVA_EMPLOYEE 		("TivaEmployeeService"),
	KV_REQUEST 			("KvRequestService"),
	KV_MESSAGE 			("KvMessageService"),
	LOORA_VALTAKIRJA  	("KokuLooraValtakirjaService"),
	LOORA_TIETOPYYNTO	("KokuLooraTietopyyntoService"),
	KUNPO_VALTAKIRJA	("KokuKunpoValtakirjaService"),
	KUNPO_REQUEST 		("KokuKunpoRequestService"),
	AV_EMPLOYEE		 	("AvEmployeeService"),
	AV_CITIZEN			("AvCitizenService");
	
	private final String value;
	
	KokuWebServices(String v) {
	    value = v;
	}
	
	public String value() {
	    return value;
	}
	
	public static KokuWebServices fromValue(String v) {
	    for (KokuWebServices c: KokuWebServices.values()) {
	        if (c.toString().equals(v)) {
	            return c;
	        }
	    }
	    throw new IllegalArgumentException(v);
	}	
}
