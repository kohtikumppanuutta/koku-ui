package fi.arcusys.koku.palvelut.controller;

import static fi.arcusys.koku.util.Constants.ATTR_PREFERENCES;
import static fi.arcusys.koku.util.Constants.JSON_LOGIN_STATUS;
import static fi.arcusys.koku.util.Constants.JSON_RESULT;
import static fi.arcusys.koku.util.Constants.PORTAL_GATEIN;
import static fi.arcusys.koku.util.Constants.PREF_SHOW_ONLY_FORM_BY_DESCRIPTION;
import static fi.arcusys.koku.util.Constants.PREF_SHOW_ONLY_FORM_BY_ID;
import static fi.arcusys.koku.util.Constants.PREF_SHOW_TASKS_BY_ID;
import static fi.arcusys.koku.util.Constants.RESPONSE;
import static fi.arcusys.koku.util.Constants.TOKEN_STATUS_INVALID;
import static fi.arcusys.koku.util.Properties.IS_KUNPO_PORTAL;
import static fi.arcusys.koku.util.Properties.VETUMA_ENABLED;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.stream.XMLStreamException;

import net.sf.json.JSONObject;

import org.intalio.tempo.workflow.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.palvelut.exceptions.IllegalOperationCall;
import fi.arcusys.koku.palvelut.util.AjaxViewResolver;
import fi.arcusys.koku.palvelut.util.OperationsValidator;
import fi.arcusys.koku.palvelut.util.OperationsValidatorImpl;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenResolver;
import fi.arcusys.koku.palvelut.util.XmlProxy;
import fi.arcusys.koku.util.AuthenticationUtil;
import fi.arcusys.koku.util.KokuWebServicesJS;
import fi.koku.settings.KoKuPropertiesUtil;


@Controller("viewController")
@RequestMapping(value = "VIEW")
public class ViewController extends FormHolderController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ViewController.class);
	
	public static final String FORM_VIEW_ACTION 						= "formview";
	public static final String VIEW_ACTION 								= "view";
	public static final String VIEW_CURRENT_FOLDER 						= "folderId";
	public static final String ADMIN_ACTION 							= "action";
	public static final String ADMIN_REMOVE_CATEGORY_ACTION 			= "removeCategory";
	public static final String ADMIN_REMOVE_FORM_ACTION 				= "removeForm";	
	public static final String ROOT_CATEGORY_LIST_MODEL_NAME 			= "rootCategories";
	

	private static final Map<String, String> JS_ENDPOINTS;
	
	static {
		Map<String, String> endpoints = new HashMap<String, String>();
		
		for (KokuWebServicesJS key : KokuWebServicesJS.values()) {
    		String value = KoKuPropertiesUtil.get(key.value());
    		if (value == null) {
    			throw new ExceptionInInitializerError("Coulnd't find property '"+ key.value()+"'");
    		}
    		if (value.endsWith("?wsdl")) {
    			int end = value.indexOf("?wsdl");
    			value = value.substring(0, end);
    		} 
    		endpoints.put(key.value(), value);
    		LOG.info("Added new endpoint to WsProxyServlet: "+value);
		}
		JS_ENDPOINTS = Collections.unmodifiableMap(endpoints);
	}
	
	
	@ResourceMapping(value = "servicesAjax")
	public String servicesAjax(ModelMap modelmap, PortletRequest request, PortletResponse response) {
		JSONObject obj = new JSONObject();
		obj.element("endpoints" , JS_ENDPOINTS.keySet());
		modelmap.addAttribute(RESPONSE, obj);
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	@ResourceMapping(value = "intalioAjax")
	public String intalioAjax(
			@RequestParam(value = "service") String service,
			@RequestParam(value = "data") String data,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		LOG.debug("Service: '"+service+"' Messsage: '"+data+"'");
		if (service.isEmpty()) {
			LOG.error("AjaxMessage Command is empty");
			returnEmptyString(modelmap);
			return AjaxViewResolver.AJAX_PREFIX;
		}
		
		if (data.isEmpty()) {
			LOG.error("AjaxMessage Data is empty");
			returnEmptyString(modelmap);
			return AjaxViewResolver.AJAX_PREFIX;
		}
		
		String result = null;
		XmlProxy proxy = getProxy(service, data);
		
		if (proxy != null) {
			try {
				result = proxy.send();				
			} catch (IllegalOperationCall ioc) {
				LOG.error("Illegal operation call. User '" + request.getUserPrincipal().getName() + "' tried to call restricted method that he/she doesn't have sufficient permission. ");
			} catch (XMLStreamException xse) {
				LOG.error("Unexpected XML-parsing error. User '" + request.getUserPrincipal().getName() + "'", xse);
			} catch (Exception e) {
				LOG.error("Coulnd't send given message. Parsing error propably. ", e);
			}
		} 
		
		JSONObject obj = new JSONObject();
		if (result == null || result.isEmpty()) {
			obj.element(JSON_RESULT, "");			
		} else {
			obj.element(JSON_RESULT, result);
		}
		modelmap.addAttribute(RESPONSE, obj);
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	private XmlProxy getProxy(String service, String data) {
		
		OperationsValidator validator = new OperationsValidatorImpl();
		validator = null;
		String endpointUrl = JS_ENDPOINTS.get(service);
		if (endpointUrl == null) {
			LOG.error("Coulnd't create XMLProxy. No service found by given service name: '"+service+"'");
			return null;
		} else {
			return new XmlProxy("", endpointUrl, data, validator);
		}
	}
	
	private ModelMap returnEmptyString(ModelMap modelmap) {
		JSONObject obj = new JSONObject();
		obj.element(JSON_RESULT, "");
		modelmap.addAttribute(RESPONSE, obj);
		return modelmap;
	}
	
	
	/*
	 * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	@RenderMapping
	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderRequest response) throws Exception {

		final String username = request.getRemoteUser();
		PortletSession portletSession = request.getPortletSession();
		if (isInvalidStrongAuthentication(portletSession)) {
			return authenticationFailed(request);
		}
		
		if (username == null) {
			LOG.info("Can't show Intalio form. User not logged in");
			return getFailureView(request);
		}
		
		final PortletPreferences prefs = request.getPreferences();
		final Boolean showFormById = Boolean.valueOf(prefs.getValue(PREF_SHOW_TASKS_BY_ID, null));
		final String formId = prefs.getValue(PREF_SHOW_ONLY_FORM_BY_ID, null);
		final String formDescription = prefs.getValue(PREF_SHOW_ONLY_FORM_BY_DESCRIPTION, null);
		LOG.debug("showFormById " + showFormById);
		
		ModelAndView mav = new ModelAndView(FORM_VIEW_ACTION, "model", null);
		mav.addObject(ATTR_PREFERENCES, request.getPreferences());
		Task t = null;	
		TokenResolver tokenUtil = new TokenResolver();
		try {
			if (showFormById) {
				LOG.debug("Loading taskID: '" +formId+"'");
				t = TaskUtil.getTask(tokenUtil.getAuthenticationToken(request), formId);
			} else {
				t = TaskUtil.getTaskByDescription(tokenUtil.getAuthenticationToken(request), formDescription);
				// Fallback. Try to get form by Id
				if (t == null) {
					LOG.error("Fallback! Couldn't find task by description name. Trying to get form by Id. Description: '" + formDescription + "'");
					t = TaskUtil.getTask(tokenUtil.getAuthenticationToken(request), formId);
				}
				LOG.debug("Loading taskDescription: '" +t.getDescription()+ "' Id: '"+t.getID()+"'");
			}
		} catch (Exception e) {
			if (request.getUserPrincipal() != null && request.getUserPrincipal().getName() != null) {
				LOG.error("Failure while trying to get Task. Use FormId: '"+showFormById+"' Description: '"+formDescription+"' Username: '"+request.getUserPrincipal().getName()+"'. Some hints to fix problem: " +
						"\n\t1. Logged in proper user? (this portlet doesn't work correctly with admin/nonlogged users. " +
						"\n\t2. Task might be updated. Reselect form in 'edit'-mode. " +
						"\n\t3. Check that connection to Intalio server is up. ", e.getMessage());				
			}
			return getFailureView(request);
		}
		
		if (t == null) {
			LOG.error("Coulnd't find task by descrption or formId! Description: '"+formDescription+"' FormId: '"+formId+"'");
		}
		
		mav.addObject("formholder", getFormHolderFromTask(request, t.getDescription()));
		return mav;
	}
	
	private ModelAndView getFailureView(RenderRequest request) {
		ModelAndView failureMav = new ModelAndView("failureView", "model", null);
		failureMav.addObject("prefs", request.getPreferences());
		return failureMav;
	}
	
	
	@ActionMapping(value="NORMAL")
	public void handleActionRequestInternal( ActionRequest request, ActionResponse response ) throws Exception {
	}
	
	@ResourceMapping(value="NORMAL")
	public void handleResourceRequestInteral(ResourceRequest request, ResourceResponse response ) throws Exception {
		
	}
	
	/**
	 * Returns true when following conditions are true:
	 * <ul>
	 * <li>Vetuma is enabled (vetuma.authentication=true)</li>
	 * <li>User is _NOT_ authenticated by using strong authentication (Vetuma)</li>
	 * <li>Portal is Kunpo (enviroment.name=kunpo)</li>
	 * </ul>
	 * @param portletSession
	 * @return 
	 */
	private boolean isInvalidStrongAuthentication(PortletSession portletSession) {
		return false;
//		if (VETUMA_ENABLED && IS_KUNPO_PORTAL && !AuthenticationUtil.getUserInfoFromSession(portletSession).hasStrongAuthentication()) {
//			return true;
//		} else {
//			return false;
//		}
	}
	
	private ModelAndView authenticationFailed(RenderRequest request){
		LOG.error("Strong authentication required! User '"+request.getUserPrincipal().getName()+"' is not Vetuma authenticated!");	
		return getFailureView(request);
	}
	
}
