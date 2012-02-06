package fi.arcusys.koku.web;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import static fi.arcusys.koku.util.Constants.*;

/**
 * Handles the action in configuration mode,
 * @author Jinhua Chen
 * Jun 22, 2011
 */
@Controller("configController")
@RequestMapping(value = "EDIT")
public class ConfigController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);
	
	@RenderMapping
	public String showConfig(RenderRequest request, RenderResponse response, ModelMap modelmap) {	
		LOG.debug("show edit mode");
		return VIEW_CONFIG;
	}
	
	@ActionMapping(params="myaction=config")
	public void doConfig(ActionRequest request, ActionResponse response)  {
		 
        try { 
            PortletPreferences pref = request.getPreferences();       
    		String refreshDuration = request.getParameter(PREF_REFRESH_DURATION);
    		String messageType = request.getParameter(PREF_MESSAGE_TYPE);
    		LOG.info("User '"+request.getRemoteUser() + "' saved settings: refresh: " + refreshDuration + " messageType: " + messageType);
    		pref.setValue(PREF_REFRESH_DURATION, refreshDuration);
    		pref.setValue(PREF_MESSAGE_TYPE, messageType);
    		pref.store();
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
 
        } catch (Exception e) { 
        	LOG.error("Configuration edit mode failed with exception");
        }         
	}	

}
