package fi.arcusys.koku.navi;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.util.Properties;

/**
 * Handles the controller for navigation page
 * @author Jinhua Chen
 * Jun 22, 2011
 */
@Controller("naviController")
@RequestMapping(value = "VIEW")
public class NaviController {
	private static final Logger LOG = Logger.getLogger(NaviController.class);
	
	// maps the incoming portlet request to this method, returns the default page
	@RenderMapping
	public String home(RenderRequest request, RenderResponse response, ModelMap modelmap) {
		if (Properties.IS_KUNPO_PORTAL) {
			return "navi_kunpo";			
		} else if (Properties.IS_LOORA_PORTAL) {
			return "navi_loora";
		} else {
			LOG.error("Coulnd't determine proper portalMode. Please check properties files and/or code for possible errors.");
			return "error";
		}
	}

	// @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "loginStatus")
	public String model(RenderRequest request) {
		String user = request.getRemoteUser();
		if(user != null) {
			return "VALID";
		} else {
			LOG.info("no logged in user");
			return "INVALID";
		}			
	}
	
	/**
	 * Shows navigation page according to portlet request and stores the navigation type
	 * @param naviType page name in navigation
	 * @param request RenderRequest
	 * @param response RenderResponse
	 * @param modelmap ModelMap
	 * @return navigation page with navigation type
	 */
	@RenderMapping(params = "myaction=showNavi")
	public String showNavi(@RequestParam(value = "naviType", required = false) String naviType,
			RenderRequest request, RenderResponse response, ModelMap modelmap) {
		modelmap.addAttribute("naviType", naviType);
		
		return "navi";
	}

}
