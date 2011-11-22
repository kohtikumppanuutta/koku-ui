package fi.arcusys.koku.util;

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

	public static final boolean IS_KUNPO_PORTAL;
	public static final boolean IS_LOORA_PORTAL;
	public static final boolean VETUMA_ENABLED;
	/** Portal mode has two possible values: "kunpo" / "loora" */
	public static final String PORTAL_MODE;
	
	static {
		final String envName = KoKuPropertiesUtil.get(Constants.PROPERTIES_ENVIROMENT_NAME);
		final String vetumaEnabled = KoKuPropertiesUtil.get(Constants.PROPERTIES_VETUMA_ENABLED);

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
		
	}
}
