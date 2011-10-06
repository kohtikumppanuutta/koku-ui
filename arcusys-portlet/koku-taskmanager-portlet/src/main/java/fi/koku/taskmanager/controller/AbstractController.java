package fi.koku.taskmanager.controller;

import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import fi.arcusys.koku.util.PortalRole;

public class AbstractController {
	
	private Logger LOG = Logger.getLogger(AbstractController.class);

	
	/**
	 * Resolves which portalRole portal has
	 * 
	 * FIXME: Super stupid portalRole resolving. For now
	 * we determine if portal is citizen or employee only by portal name. 
	 * 
	 * 
	 * @param request PortletRequest
	 * @return PortalRole
	 */
	protected PortalRole getPortalRole(PortletRequest request) {
		
		LOG.debug("Currently used portal: " + request.getPortalContext().getPortalInfo());
		String portalInfo = request.getPortalContext().getPortalInfo();
		if (portalInfo.contains("EPP") || portalInfo.contains("Enterprise")) {
			return PortalRole.CITIZEN;
		} else {
			return PortalRole.EMPLOYEE;
		}
	}
}
