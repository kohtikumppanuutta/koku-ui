package fi.arcusys.koku.navi;

import static fi.arcusys.koku.util.Constants.*;

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
import org.apache.log4j.Logger;


@Controller("naviEditController")
@RequestMapping(value = "EDIT")
public class EditController {
	
	private Logger LOG = Logger.getLogger(EditController.class);
	
	@RenderMapping
	public String showConfig(RenderRequest request, RenderResponse response, ModelMap modelmap) {	
		LOG.debug("show navi edit mode");
		return VIEW_NAVI_CONFIG;
	}
	
	@ActionMapping(params="myaction=naviconfig")
	public void doConfig(ActionRequest request, ActionResponse response)  {
		 
        try { 
            PortletPreferences pref = request.getPreferences();       
    		String useRelativePath = request.getParameter(PREF_NAVI_RELATIVE_PATH);
    		String kksPref = request.getParameter(PREF_NAVI_KKS);
    		String lokPref = request.getParameter(PREF_NAVI_LOK);
    		String pyhPref = request.getParameter(PREF_NAVI_PYH);
    		String defaultPathPref = request.getParameter(PREF_NAVI_DEFAULT_PATH);
    		pref.setValue(PREF_NAVI_RELATIVE_PATH, useRelativePath);
    		pref.setValue(PREF_NAVI_KKS, kksPref);
    		pref.setValue(PREF_NAVI_LOK, lokPref);
    		pref.setValue(PREF_NAVI_PYH, pyhPref);
    		pref.setValue(PREF_NAVI_DEFAULT_PATH, defaultPathPref);
    		pref.store();
    		LOG.info("KokuNavigationPortlet - User '" + request.getUserPrincipal().getName() + "' saved new settings - RelativePathMode: '"+useRelativePath+"' Lok path: '"+lokPref+"' Pyh path: '"+pyhPref+"' Kks path: '"+kksPref+"'");    		
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
 
        } catch (Exception e) { 
        	LOG.error("Configuration edit mode failed with exception");
        }         
	}	

}