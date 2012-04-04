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
public class Properties {
	
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
	
	
	
	static {
		final String envName = KoKuPropertiesUtil.get(Constants.PROPERTIES_ENVIROMENT_NAME);
		final String vetumaEnabled = KoKuPropertiesUtil.get(Constants.PROPERTIES_VETUMA_ENABLED);
		final String reqFilter = KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_REQUESTS);
		final String infoReqFilter = KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_INFO_REQUESTS);
		final String applicationFilter = KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_APPLICATIONS);
		final String warrantsFilter = KoKuPropertiesUtil.get(PROPERTIES_FILTER_RECIEVED_WARRANTS);		

		PORTAL_MODE = envName;
		if (PORTAL_MODE == null) {
			throw new ExceptionInInitializerError("environment.name is null or doesn't exists in koku-settings.properties file!");
		}
		
		IS_KUNPO_PORTAL = (envName != null && envName.equalsIgnoreCase(Constants.PORTAL_MODE_KUNPO) ? true : false);
		IS_LOORA_PORTAL = (envName != null && envName.equalsIgnoreCase(Constants.PORTAL_MODE_LOORA) ? true : false);
		if (vetumaEnabled != null && vetumaEnabled.equalsIgnoreCase(Boolean.TRUE.toString())) {
			LOG.info("Vetuma  authentication enabled");
			VETUMA_ENABLED = true;
		} else {
			LOG.info("Vetuma authentication disabled");
			VETUMA_ENABLED = false;
		}
		
		
		RECEIVED_REQUESTS_FILTER =  loadProperty("Received requests filter (Pyynnöt - Saapuneet)", reqFilter);
		RECEIVED_INFO_REQUESTS_FILTER =  loadProperty("Received inforequests filter (Tietopyynnöt - Saapuneet)", infoReqFilter);
		RECEIVED_KINDERGARTED_APPLICATION_FILTER = loadProperty("Received applications filter (Asiointipalvelut - Hakemusten vahvistuspyynnöt)", applicationFilter);		
		RECEIVED_WARRANTS_FILTER = loadProperty("Received warrants filter (Valtakirjat - Valtuutettuna)", warrantsFilter);
		
	}
	
	public static String loadProperty(String message, String property) {
		LOG.info(message + ": "+ property);
		if (property == null) {
			LOG.warn(message + " value is null!");
			return null;
		} else {
			return property.trim();			
		}
	}
}
