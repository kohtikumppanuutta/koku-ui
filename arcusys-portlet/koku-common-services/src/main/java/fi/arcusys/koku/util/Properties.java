package fi.arcusys.koku.util;

import static fi.arcusys.koku.util.Constants.PROPERTIES_FILTER_RECIEVED_APPLICATIONS;
import static fi.arcusys.koku.util.Constants.PROPERTIES_FILTER_RECIEVED_INFO_REQUESTS;
import static fi.arcusys.koku.util.Constants.PROPERTIES_FILTER_RECIEVED_REQUESTS;
import static fi.arcusys.koku.util.Constants.PROPERTIES_FILTER_RECIEVED_WARRANTS;

import org.apache.log4j.Logger;

import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Class that handles koku-settings.properties key/value loading for 
 * arcusys-portlets
 * 
 * @author Toni Turunen
 *
 */
public final class Properties {
	
	private final static Logger LOG = Logger.getLogger(Properties.class);
	
	private Properties() { /* Not instantiable */ }

	public static final boolean IS_KUNPO_PORTAL;
	public static final boolean IS_LOORA_PORTAL;
	public static final boolean VETUMA_ENABLED;
	/** Portal mode has two possible values: "kunpo" / "loora" */
	public static final String PORTAL_MODE;
	
	/* Filtering strings for  */
	public static final String RECEIVED_REQUESTS_FILTER;
	public static final String RECEIVED_INFO_REQUESTS_FILTER;
	public static final String RECEIVED_KINDERGARTED_APPLICATION_FILTER;
	public static final String RECEIVED_WARRANTS_FILTER;
	
	/* WebServices */
	public static final String USER_SERVICE;
	public static final String TIVA_CITIZEN_SERVICE; 
	public static final String TIVA_EMPLOYEE_SERVICE;
	public static final String KV_REQUEST_SERVICE;
	public static final String KV_MESSAGE_SERVICE;
	public static final String KOKU_LOORA_VALTAKIRJA_SERVICE;
	public static final String KOKU_LOORA_TIETOPYYNTO_SERVICE;
	public static final String KOKU_KUNPO_VALTAKIRJA_SERVICE;
	public static final String KOKU_KUNPO_REQUEST_SERVICE;  
    public static final String AV_EMPLOYEE_SERVICE; 
    public static final String AV_CITIZEN_SERVICE;
	
    /* Intalio WebServices */
    public static final String INTALIO_TOKEN_SERVICE;
    public static final String INTALIO_TASKMGR_SERVICE;
	
	static {
		final String envName = KoKuPropertiesUtil.get(Constants.PROPERTIES_ENVIROMENT_NAME);
		final String vetumaEnabled = KoKuPropertiesUtil.get(Constants.PROPERTIES_VETUMA_ENABLED);

		PORTAL_MODE = envName;
		if (PORTAL_MODE == null) {
			throw new ExceptionInInitializerError("environment.name is null or doesn't exists in koku-settings.properties file!");
		}
		
		IS_KUNPO_PORTAL = (envName.equalsIgnoreCase(Constants.PORTAL_MODE_KUNPO) ? true : false);
		IS_LOORA_PORTAL = (envName.equalsIgnoreCase(Constants.PORTAL_MODE_LOORA) ? true : false);
		if (vetumaEnabled != null && vetumaEnabled.equalsIgnoreCase(Boolean.TRUE.toString())) {
			LOG.info("Vetuma  authentication enabled");
			VETUMA_ENABLED = true;
		} else {
			LOG.info("Vetuma authentication disabled");
			VETUMA_ENABLED = false;
		}
		
		/* Filters for TaskMgr and Navi counters (Intalio related) */
		RECEIVED_REQUESTS_FILTER =  loadProperty("Received requests filter (Pyynnöt - Saapuneet)", PROPERTIES_FILTER_RECIEVED_REQUESTS);
		RECEIVED_INFO_REQUESTS_FILTER =  loadProperty("Received inforequests filter (Tietopyynnöt - Saapuneet)", PROPERTIES_FILTER_RECIEVED_INFO_REQUESTS);
		RECEIVED_KINDERGARTED_APPLICATION_FILTER = loadProperty("Received applications filter (Asiointipalvelut - Hakemusten vahvistuspyynnöt)", PROPERTIES_FILTER_RECIEVED_APPLICATIONS);		
		RECEIVED_WARRANTS_FILTER = loadProperty("Received warrants filter (Valtakirjat - Valtuutettuna)", PROPERTIES_FILTER_RECIEVED_WARRANTS);
		
		/* WebServices for portlets */
		USER_SERVICE =  loadProperty("UsersAndGroupsService location:", "UsersAndGroupsService");
		TIVA_CITIZEN_SERVICE =  loadProperty("TivaCitizenService location:", "TivaCitizenService");
		TIVA_EMPLOYEE_SERVICE =  loadProperty("TivaEmployeeService location:", "TivaEmployeeService");
		KV_REQUEST_SERVICE =  loadProperty("KvRequestService location:", "KvRequestService");
		KV_MESSAGE_SERVICE =  loadProperty("KvMessageService location:", "KvMessageService");
		KOKU_LOORA_VALTAKIRJA_SERVICE =  loadProperty("KokuLooraValtakirjaService location:", "KokuLooraValtakirjaService");
		KOKU_LOORA_TIETOPYYNTO_SERVICE =  loadProperty("KokuLooraTietopyyntoService location:", "KokuLooraTietopyyntoService");
		KOKU_KUNPO_VALTAKIRJA_SERVICE =  loadProperty("KokuKunpoValtakirjaService location:", "KokuKunpoValtakirjaService");
		KOKU_KUNPO_REQUEST_SERVICE =  loadProperty("KokuKunpoRequestService location:", "KokuKunpoRequestService");
		AV_EMPLOYEE_SERVICE =  loadProperty("AvEmployeeService location:", "AvEmployeeService");
		AV_CITIZEN_SERVICE =  loadProperty("AvCitizenService location:", "AvCitizenService");
		
		INTALIO_TOKEN_SERVICE = loadProperty("TokenService location:", "TokenService");
		INTALIO_TASKMGR_SERVICE = loadProperty("TaskManagerService location:", "TaskManagerService");
	}
	
	public static String loadProperty(final String message, final String propertyName) {
		final String value = KoKuPropertiesUtil.get(propertyName);
		LOG.info(message + ": "+ value);
		if (value == null) {
			LOG.warn(message + " value is null!");
			return null;
		} else {
			return value.trim();
		}		
	}
	
}
