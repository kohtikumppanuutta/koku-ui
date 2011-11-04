package fi.arcusys.koku.web;

import static fi.arcusys.koku.util.Constants.PORTAL_MODE_KUNPO;
import static fi.arcusys.koku.util.Constants.PORTAL_MODE_LOORA;

import fi.arcusys.koku.util.PortalRole;
import fi.koku.settings.KoKuPropertiesUtil;

public class AbstractController {
	
	private static final String PORTAL_MODE;
	
	static {
		PORTAL_MODE = KoKuPropertiesUtil.get("environment.name");
		if (PORTAL_MODE == null) {
			throw new ExceptionInInitializerError("environment.name is null!");
		}
	}
	
	/**
	 * Resolves which portalRole portal has
	 * 
	 * @return PortalRole
	 */
	protected PortalRole getPortalRole() {		
		if (PORTAL_MODE.contains(PORTAL_MODE_KUNPO)) {
			return PortalRole.CITIZEN;
		} else if (PORTAL_MODE.contains(PORTAL_MODE_LOORA)) {
			return PortalRole.EMPLOYEE;
		} else {
			throw new IllegalArgumentException("PortalMode not supported: "+ PORTAL_MODE);
		}		
	}
}
