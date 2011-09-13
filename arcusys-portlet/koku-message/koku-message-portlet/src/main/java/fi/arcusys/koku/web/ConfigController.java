package fi.arcusys.koku.web;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Handles the action in configuration mode,
 * @author Jinhua Chen
 * Jun 22, 2011
 */
@Controller("configController")
@RequestMapping(value = "EDIT")
public class ConfigController {
	
	private Logger logger = Logger.getLogger(ConfigController.class);
	
	@RenderMapping
	public String showConfig(RenderRequest request, RenderResponse response, ModelMap modelmap) {	
		System.out.println("show edit mode");
		return "config";
	}
	
	@ActionMapping(params="myaction=config")
	public void doConfig(ActionRequest request, ActionResponse response)  {
		 
        try { 
            PortletPreferences pref = request.getPreferences();       
    		String refreshDuration = request.getParameter("refreshDuration");
    		String messageType = request.getParameter("messageType");
    		System.out.println("refresh: " + refreshDuration
    				+ "messageType: " + messageType);
    		pref.setValue("refreshDuration", refreshDuration);
    		pref.setValue("messageType", messageType);
    		pref.store();
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
 
        } catch (Exception e) { 
        	logger.error("Configuration edit mode failed with exception");
        }         
	}	

}