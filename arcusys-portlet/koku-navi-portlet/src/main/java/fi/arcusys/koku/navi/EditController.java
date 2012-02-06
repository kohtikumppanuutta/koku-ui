package fi.arcusys.koku.navi;

import static fi.arcusys.koku.util.Constants.PREF_NAVI_DEFAULT_PATH;
import static fi.arcusys.koku.util.Constants.PREF_NAVI_FRONTPAGE;
import static fi.arcusys.koku.util.Constants.PREF_NAVI_KKS;
import static fi.arcusys.koku.util.Constants.PREF_NAVI_LOK;
import static fi.arcusys.koku.util.Constants.PREF_NAVI_PYH;
import static fi.arcusys.koku.util.Constants.PREF_NAVI_RELATIVE_PATH;
import static fi.arcusys.koku.util.Constants.VIEW_NAVI_CONFIG;

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


@Controller("naviEditController")
@RequestMapping(value = "EDIT")
public class EditController {
	
	private static final Logger LOG = LoggerFactory.getLogger(EditController.class);
	
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
    		String frontPagePath = request.getParameter(PREF_NAVI_FRONTPAGE);
    		pref.setValue(PREF_NAVI_RELATIVE_PATH, useRelativePath);
    		pref.setValue(PREF_NAVI_KKS, kksPref);
    		pref.setValue(PREF_NAVI_LOK, lokPref);
    		pref.setValue(PREF_NAVI_PYH, pyhPref);
    		pref.setValue(PREF_NAVI_DEFAULT_PATH, defaultPathPref);
    		pref.setValue(PREF_NAVI_FRONTPAGE, frontPagePath);
    		pref.store();
    		LOG.info("KokuNavigationPortlet - User '" + request.getUserPrincipal().getName() 
    				+ "' saved new settings - RelativePathMode: '" + useRelativePath + "' Lok path: '"
    				+ lokPref + "' Pyh path: '" + pyhPref + "' Kks path: '" + kksPref + "' "
    				+ "' MessagePortlet default path: '" + defaultPathPref + "'" 
    				+ " FrontPagePath: '" + frontPagePath + "'");    		
    		
    		response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL); 
        } catch (Exception e) { 
        	LOG.error("Configuration edit mode failed with exception. ErrorMsg: "+ e.getMessage());
        }         
	}	

}