package fi.koku.taskmanager.controller;

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
 * Configuration/edit mode of portlet
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("configController")
@RequestMapping(value = "EDIT")
public class ConfigController {

	private Logger logger = Logger.getLogger(ConfigController.class);
	@RenderMapping
	public String showConfig(RenderRequest request, RenderResponse response, ModelMap modelmap) {	
		
		return "config";
	}
	
	@ActionMapping(params="myaction=config")
	public void doConfig(ActionRequest request, ActionResponse response)  {
		 
        try { 
            PortletPreferences pref = request.getPreferences();       
            String taskFilter = request.getParameter("taskFilter");
    		String notifFilter = request.getParameter("notifFilter");
    		String refreshDuration = request.getParameter("refreshDuration");
    		String openForm = request.getParameter("openForm");
    		String defaultTaskType = request.getParameter("defaultTaskType");
    		pref.setValue("taskFilter", taskFilter);
    		pref.setValue("notifFilter", notifFilter);
    		pref.setValue("refreshDuration", refreshDuration);
    		pref.setValue("openForm", openForm);
    		pref.setValue("defaultTaskType", defaultTaskType);
    		pref.store();
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
 
        } catch (Exception e) { 
        	logger.error("Configure exception");
        }        
        
	}	

}
