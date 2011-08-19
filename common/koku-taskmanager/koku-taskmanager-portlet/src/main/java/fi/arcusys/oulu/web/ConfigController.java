package fi.arcusys.oulu.web;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

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

	
	@RenderMapping
	public String showConfig(RenderRequest request, RenderResponse response, ModelMap modelmap) {	
		System.out.println("show edit mode");
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
    		System.out.println("taskFilter: " + taskFilter + "notifFilter: " 
    				+ notifFilter + "refresh: " + refreshDuration
    				+ "openForm: " + openForm);
    		pref.setValue("taskFilter", taskFilter);
    		pref.setValue("notifFilter", notifFilter);
    		pref.setValue("refreshDuration", refreshDuration);
    		pref.setValue("openForm", openForm);
    		pref.store();
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
 
        } catch (Exception e) { 
 
        }        
        
	}	

}
