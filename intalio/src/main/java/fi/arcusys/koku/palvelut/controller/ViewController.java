package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.xml.stream.XMLStreamException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fi.arcusys.koku.palvelut.exceptions.IllegalOperationCall;
import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.services.VeeraServicesFacade;
import fi.arcusys.koku.palvelut.util.AjaxViewResolver;
import fi.arcusys.koku.palvelut.util.CategoryUtil;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.OperationsValidator;
import fi.arcusys.koku.palvelut.util.OperationsValidatorImpl;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.XmlProxy;
import static fi.arcusys.koku.palvelut.util.Constants.*;


@Controller("viewController")
@RequestMapping(value = "VIEW")
public class ViewController extends FormHolderController {
	private static Logger LOG = Logger.getLogger(ViewController.class);
	public static final String FORM_VIEW_ACTION 						= "formview";
	public static final String VIEW_ACTION 								= "view";
	public static final String VIEW_CURRENT_FOLDER 						= "folderId";
	public static final String ADMIN_ACTION 							= "action";
	public static final String ADMIN_REMOVE_CATEGORY_ACTION 			= "removeCategory";
	public static final String ADMIN_REMOVE_FORM_ACTION 				= "removeForm";	
	public static final String ROOT_CATEGORY_LIST_MODEL_NAME 			= "rootCategories";
	

	private static final String APPOINTMENT_PROCESS_SERVICE				= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-av-model-0.1-SNAPSHOT/KokuAppointmentProcessingServiceImpl";
	private static final String APPOINTMENT_SERVICE_CITIZEN				= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-av-model-0.1-SNAPSHOT/KokuKunpoAppointmentServiceImpl";
	private static final String APPOINTMENT_SERVICE_EMPLOYEE			= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-av-model-0.1-SNAPSHOT/KokuLooraAppointmentServiceImpl";
	private static final String REQUEST_SERVICE		 					= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-kv-model-0.1-SNAPSHOT/KokuRequestServiceImpl";
	private static final String MESSAGE_SERVICE							= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-kv-model-0.1-SNAPSHOT/KokuMessageServiceImpl";	
	private static final String TIVA_CITIZEN_SERVICE					= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-tiva-model-0.1-SNAPSHOT/KokuLooraSuostumusServiceImpl";
	private static final String TIVA_EMPLOYEE_SERVICE					= "http://gatein.intra.arcusys.fi:8080/arcusys-koku-0.1-SNAPSHOT-tiva-model-0.1-SNAPSHOT/KokuKunpoSuostumusServiceImpl";


	@Autowired
	private VeeraServicesFacade servicesFacade;
	
	@ResourceMapping(value = "intalioAjax")
	public String intalioAjax(
			@RequestParam(value = "service") String service,
			@RequestParam(value = "data") String data,
			ModelMap modelmap, PortletRequest request, PortletResponse response) {
		
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
			obj.element("result", "");			
		} else {
			obj.element("result", result);
		}
		modelmap.addAttribute("response", obj);
		return AjaxViewResolver.AJAX_PREFIX;
	}
	
	private XmlProxy getProxy(String service, String data) {
		
		OperationsValidator validator = new OperationsValidatorImpl();
		validator = null;
		if (service.equals(APPOINTMENT_SERVICE_CITIZEN_NAME)) {
			return new XmlProxy("", APPOINTMENT_SERVICE_CITIZEN, data, validator);	
		} else if (service.equals(APPOINTMENT_SERVICE_EMPLOYEE_NAME)) {
			return new XmlProxy("", APPOINTMENT_SERVICE_EMPLOYEE, data, validator);	
		} else if (service.equals(MESSAGE_SERVICE_NAME)) {
			return new XmlProxy("", REQUEST_SERVICE, data, validator);
		} else if (service.equals(REQUEST_SERVICE_NAME)) {
			return new XmlProxy("", MESSAGE_SERVICE, data, validator);
		} else if (service.equals(TIVA_CITIZEN_SERVICE_NAME)) {
			return new XmlProxy("", TIVA_CITIZEN_SERVICE, data, validator);
		} else if (service.equals(TIVA_EMPLOYEE_SERVICE_NAME)) {
			return new XmlProxy("", TIVA_EMPLOYEE_SERVICE, data, validator);	
		} else {
			return null;
		}
	}
	
	private ModelMap returnEmptyString(ModelMap modelmap) {
		JSONObject obj = new JSONObject();
		obj.element("result", "");
		modelmap.addAttribute("response", obj);
		return modelmap;
	}
	
	
	/*
	 * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	@SuppressWarnings("unchecked")
	@RenderMapping
	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderRequest response) throws Exception {

		LOG.debug("handleRenderRequestInternal");
		PortletPreferences prefs = request.getPreferences();
		if (prefs.getValue(SHOW_ONLY_CHECKED, null) != null) {
			ModelAndView mav = new ModelAndView(FORM_VIEW_ACTION, "model", null);
			mav.addObject(ATTR_PREFERENCES, request.getPreferences());
			Task t = null;
			
			Boolean showFormById = Boolean.valueOf(prefs.getValue(SHOW_TASKS_BY_ID, null));
			LOG.debug("showFormById " + showFormById);
			try {
				if (showFormById) {
					t = TaskUtil.getTask(TokenUtil.getAuthenticationToken(request), prefs.getValue(SHOW_ONLY_FORM_BY_ID, null));					
				} else {
					t = TaskUtil.getTaskByDescription(TokenUtil.getAuthenticationToken(request), prefs.getValue(SHOW_ONLY_FORM_BY_DESCRIPTION, null));
					LOG.debug("t status: " + (t == null));
					// Fallback. Try to get form by Id
					if (t == null) {
						t = TaskUtil.getTask(TokenUtil.getAuthenticationToken(request), prefs.getValue(SHOW_ONLY_FORM_BY_ID, null));
					}
				}
			} catch (Exception e) {
				LOG.error("Failure while trying to get Task. Some hints to fix problem: \1. Logged in proper user? (this portlet doesn't work correctly with admin/nonlogged users \n2. Task might be updated. Reselect form in 'edit'-mode. \n3. Check that connection to Intalio server is up. ", e);
				return getFailureView(request);
			}
			FormHolder fh = getFormHolderFromTask(request, t.getDescription());
			mav.addObject("formholder", fh);
			return mav;
		}
		long companyId = MigrationUtil.getCompanyId(request);
		Integer rootFolderId = null;
		try {
			rootFolderId = getRootFolderId(companyId);			
		} catch (Exception e) {
			LOG.error("Failure while trying to get RootFolderId. See following log for more information: ", e);
			return getFailureView(request);
		}
		String folderId = request.getParameter(ViewController.VIEW_CURRENT_FOLDER);
		if (folderId == null) {
			folderId = (String) request.getAttribute(EditController.CURRENT_CATEGORY);
			if (folderId == null) {
				folderId = (String)request.getParameter(EditController.CURRENT_CATEGORY);
			}
			if (folderId == null) {
				folderId = rootFolderId.toString();
			}
		}
		int currentFolder;
		try {
			currentFolder = Integer.parseInt(folderId);
		} catch (NumberFormatException e) {
			currentFolder = rootFolderId;
		}

		try {
		
			// Category currently selected
			VeeraCategoryImpl currentCategory = servicesFacade.findCategoryByEntryAndCompanyId(currentFolder, companyId);
			if (currentCategory == null) {
				// If category doesn't exist, use root category
				currentCategory =  servicesFacade.findCategoryByEntryAndCompanyId(rootFolderId, companyId);
			}
	
			// Child categories & forms of currently selected category
			List<VeeraCategoryImpl> categories = servicesFacade.findChildCategories(currentFolder, companyId);
			for (VeeraCategoryImpl vc : categories) {
				vc.setFormCount(servicesFacade.findChildForms(vc.getEntryId()).size());
			}
		
			List<VeeraFormImpl> forms = servicesFacade.findChildFormsByFormHolders(currentFolder, companyId, getFormHoldersFromTasks(request));
			List<Object[]> formCount = (List<Object[]>) servicesFacade.findChildFormsCount(currentFolder, companyId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("categories", categories);
			map.put("forms", forms);
			map.put("rootFolderId", rootFolderId);
			map.put("currentFolder", currentFolder);
			map.put("currentCategory", currentCategory);
			map.put("childFormCount", formCount);
			String currentFolderArg = request.getParameter(ViewController.VIEW_CURRENT_FOLDER);
			if (currentFolderArg == null) {
				if (currentFolderArg == null) {
					// Form/Category edit/view or redirect from EditController
					currentFolderArg = (String) request.getAttribute(EditController.CURRENT_CATEGORY);
				}
			}
			
	
			int currentFolderId = -1;
			VeeraCategoryImpl category = null;
			try {
				currentFolderId = Integer.parseInt(currentFolderArg);
				category = servicesFacade.findCategoryByEntryAndCompanyId(currentFolderId, companyId);
			} catch (NumberFormatException e) {
				// category remains as null
			}
	
			boolean currentCategoryIsRoot = currentFolderId == rootFolderId;
			List<VeeraCategoryImpl> path = new ArrayList<VeeraCategoryImpl>();
			if (category != null && !currentCategoryIsRoot) {
				while (category != null && category.getEntryId() != rootFolderId) {
					path.add(category);
					category = servicesFacade.findCategoryByEntryAndCompanyId(category.getParent(), companyId);
				}
				path.add(category);
			}
			Collections.reverse(path);
			ModelAndView mav = new ModelAndView(VIEW_ACTION, "model", map);
			mav.addObject("breadcrumb", path);
			mav.addObject(ATTR_PREFERENCES, request.getPreferences());
			return mav;
		} catch (Exception e) {
			LOG.error("VeeraServicesFacade failure. See following log for more information: ", e);
			return getFailureView(request);
		}
	}
	
	private ModelAndView getFailureView(RenderRequest request) {
		ModelAndView failureMav = new ModelAndView("failureView", "model", null);
		failureMav.addObject("prefs", request.getPreferences());
		return failureMav;

	}
	
	private Integer getRootFolderId(long companyId) {

		List<VeeraCategoryImpl> list = servicesFacade.findAllCategoriesByCompanyId(companyId);
		int[] entryIds = CategoryUtil.getEntryIds(list);
		Integer rootFolderId = CategoryUtil.min(entryIds);
		if (rootFolderId == null) {
			createNewCategory(companyId);
			list = servicesFacade.findAllCategoriesByCompanyId(companyId);
			entryIds = CategoryUtil.getEntryIds(list);
			rootFolderId = CategoryUtil.min(entryIds);
		}
		return rootFolderId;
	}
	
	private void createNewCategory(long companyId) {
		VeeraCategoryImpl category = new VeeraCategoryImpl();
		category.setName("Palvelut");
		category.setParent(Integer.valueOf(-1));
		category.setDescription("");
		category.setCompanyId(Long.valueOf(companyId));
		servicesFacade.createCategory(category);
	}
	
	@ActionMapping(value="NORMAL")
	public void handleActionRequestInternal( ActionRequest request, ActionResponse response ) throws Exception {
	}
	
	@ResourceMapping(value="NORMAL")
	public void handleResourceRequestInteral(ResourceRequest request, ResourceResponse response ) throws Exception {
		
	}
}
