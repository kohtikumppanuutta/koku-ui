package fi.koku.taskmanager.controller;

import static fi.arcusys.koku.util.Constants.PREF_DEFAULT_TASK_TYPE;
import static fi.arcusys.koku.util.Constants.PREF_EDITABLE;
import static fi.arcusys.koku.util.Constants.PREF_NOTIFICATION_FILTER;
import static fi.arcusys.koku.util.Constants.PREF_OPEN_FORM;
import static fi.arcusys.koku.util.Constants.PREF_REFRESH_DURATION;
import static fi.arcusys.koku.util.Constants.PREF_TASK_FILTER;
import static fi.arcusys.koku.util.Constants.VIEW_CONFIG;

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
 * 
 * @author Jinhua Chen
 * May 11, 2011
 */
@Controller("configController")
@RequestMapping(value = "EDIT")
public class ConfigController {

	private static final Logger LOG = Logger.getLogger(ConfigController.class);
	
	/**
	 * Returns configuration view
	 * 
	 * @param request
	 * @param response
	 * @param modelmap
	 * @return configuration view
	 */
	@RenderMapping
	public String showConfig(RenderRequest request, RenderResponse response, ModelMap modelmap) {	
		return VIEW_CONFIG;
	}
	
	/**
	 * Save taskmanager configuration settings (Works only Kunpo(GateIn side)
	 * 
	 * @param request
	 * @param response
	 */
	@ActionMapping(params="myaction=config")
	public void doConfig(ActionRequest request, ActionResponse response)  {
		 
        try { 
            PortletPreferences pref = request.getPreferences();       
            final String taskFilter = request.getParameter(PREF_TASK_FILTER);
            final String notifFilter = request.getParameter(PREF_NOTIFICATION_FILTER);
            final String refreshDuration = request.getParameter(PREF_REFRESH_DURATION);
            final String openForm = request.getParameter(PREF_OPEN_FORM);
            final String defaultTaskType = request.getParameter(PREF_DEFAULT_TASK_TYPE);
            final String editable = request.getParameter(PREF_EDITABLE);
    		pref.setValue(PREF_TASK_FILTER, taskFilter);
    		pref.setValue(PREF_NOTIFICATION_FILTER, notifFilter);
    		pref.setValue(PREF_REFRESH_DURATION, refreshDuration);
    		pref.setValue(PREF_OPEN_FORM, openForm);
    		pref.setValue(PREF_DEFAULT_TASK_TYPE, defaultTaskType);
    		pref.setValue(PREF_EDITABLE, editable);
    		pref.store();
    		
    		LOG.info("TaskManager - User '"+request.getRemoteUser()+"' saved new settings. TaskFilter: '"
    				+taskFilter+"' NotificationFilter: '"+notifFilter+"' RefreshDuration: '"
    				+refreshDuration+"' OpenForm: '"+openForm+"' DefaultTaskType: '"+defaultTaskType+"'");
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
        } catch (Exception e) { 
        	LOG.error("Configuration exception", e);
        }        
        
	}	

}
