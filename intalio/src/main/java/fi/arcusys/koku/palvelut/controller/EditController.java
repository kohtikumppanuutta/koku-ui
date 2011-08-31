package fi.arcusys.koku.palvelut.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.palvelut.model.client.TaskHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.services.VeeraServicesFacade;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

@Controller("editController")
@RequestMapping(value = "VIEW")
public class EditController { 
	
	public static final String EDIT_TYPE 					= "editType";
	public static final String SHOW_RESULTS					= "showResults";
	public static final String SHOW_ADD_CATEGORY 			= "addCategory";
	public static final String SHOW_EDIT_CATEGORY 			= "editCategory";
	public static final String INSERT_CATEGORY 				= "insertCategory";
	public static final String UPDATE_CATEGORY 				= "updateCategory";
	public static final String CURRENT_CATEGORY 			= "categoryId";
	public static final String SHOW_ADD_INTALIO_FORM 		= "addIntalioForm";
	public static final String SHOW_ADD_URL_FORM 			= "addURLForm";
	public static final String SHOW_EDIT_FORM 				= "editForm";
	public static final String UPDATE_FORM 					= "updateForm";
	public static final String INSERT_INTALIO_FORM 			= "insertIntalioForm";
	public static final String INSERT_URL_FORM 				= "insertURLForm";
	public static final String ADMIN_REMOVE_CATEGORY_ACTION = "removeCategory";
	public static final String ADMIN_REMOVE_FORM_ACTION 	= "removeForm";
	public static final String UI_MESSAGE 					= "UIMessage";
	public static final String RESULTCODE 					= "resultCode";
	public static final String RESULTCODE_SUCCESS 			= "0";
	public static final String RESULTCODE_FAIL 				= "1";	
	public static final String BREADCRUMB 					= "breadcrumb";
	
	private static final int MAX_PARAM_LENGTH 				= 4000;
	
	private static Logger LOG = Logger.getLogger(EditController.class);

	@Autowired
	private VeeraServicesFacade servicesFacade;

	
	@RenderMapping(params="action=edit")
	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		long companyId = MigrationUtil.getCompanyId(request);

		String editType = request.getParameter(EDIT_TYPE);

		LOG.info("editType = " + editType);

		if (SHOW_ADD_CATEGORY.equals(editType)) {
			ModelAndView addCategory = new ModelAndView(SHOW_EDIT_CATEGORY);
			addCategory.addObject(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
			addCategory.addObject(BREADCRUMB, getPath(request, Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY)), companyId));
			return addCategory;
		}

		if (SHOW_EDIT_CATEGORY.equals(editType)) {
			String catArg = request.getParameter("editCategoryId");
			try {
				int categoryId = Integer.parseInt(catArg);
				VeeraCategoryImpl category = servicesFacade.findCategoryByEntryAndCompanyId(categoryId, companyId);
				if (category == null) {
					LOG.error("Invalid edit category id = " + categoryId);
					return new ModelAndView("view");
				}
				ModelAndView editCategory = new ModelAndView(SHOW_EDIT_CATEGORY);
				editCategory.addObject(SHOW_EDIT_CATEGORY, category);
				editCategory.addObject(CURRENT_CATEGORY, request
						.getParameter(CURRENT_CATEGORY));
				editCategory.addObject(BREADCRUMB, getPath(request, Integer.parseInt(request
						.getParameter(CURRENT_CATEGORY)), companyId));
				return editCategory;
			} catch (Exception e) {
				LOG.error("Invalid edit category parameter = " + catArg);
				return new ModelAndView("view");
			}
		}

		if ("addIntalioForm".equals(editType)) {
			ModelAndView addForm = new ModelAndView("addIntalioForm");
			addForm.addObject("formList", getTaskHolders(request));
			addForm.addObject(CURRENT_CATEGORY, request.getParameter(CURRENT_CATEGORY));
			addForm.addObject(BREADCRUMB, getPath(request, Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY)), companyId));
			return addForm;
		}

		if (SHOW_ADD_URL_FORM.equals(editType)) {
			ModelAndView addForm = new ModelAndView("editForm");
			addForm.addObject(CURRENT_CATEGORY, request.getParameter(CURRENT_CATEGORY));
			addForm.addObject(BREADCRUMB, getPath(request, Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY)), companyId));
			return addForm;
		}

		if ("editForm".equals(editType)) {
			String formArg = request.getParameter("formId");
			try {
				int formId = Integer.parseInt(formArg);
				VeeraFormImpl form = servicesFacade.findFormByEntryId(formId);
				if (form == null) {
					LOG.error("Invalid edit form id = " + formId);
					return new ModelAndView("view");
				}
				ModelAndView editForm = new ModelAndView("editForm");
				editForm.addObject("form", form);
				editForm.addObject(CURRENT_CATEGORY, request
						.getParameter(CURRENT_CATEGORY));
				editForm.addObject(BREADCRUMB, getPath(request, Integer.parseInt(request
						.getParameter(CURRENT_CATEGORY)), companyId));
				return editForm;
			} catch (Exception e) {
				LOG.error("Invalid edit form parameter = " + formArg);
				return new ModelAndView("view");
			}
		}

		if (SHOW_RESULTS.equals(editType)) {
			ModelAndView view = new ModelAndView("view");
			LOG.info("UIMessage = " + request.getParameter(UI_MESSAGE));
			view.addObject(UI_MESSAGE, request.getParameter(UI_MESSAGE));
			view.addObject(RESULTCODE, request.getParameter(RESULTCODE));
			view.addObject(CURRENT_CATEGORY, request.getParameter(CURRENT_CATEGORY));
			view.addObject(BREADCRUMB, getPath(request, Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY)), companyId));
			return view;
		}

		LOG.error("No view defined for EDIT_TYPE = " + editType);
		return new ModelAndView("view");
	}

	@ActionMapping(params="action=edit")
	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		String editType = request.getParameter(EDIT_TYPE);
		LOG.info("ActionRequest editType parameter = " + editType);

		long companyId = MigrationUtil.getCompanyByUser(request);
		
		if (editType.equals("removeCategory")) {
			removeCategory(request, response, companyId);
		} else if (editType.equals("removeForm")) {
			removeForm(request, response, companyId);
		} else if (editType.equals("insertIntalioForm")) {
			insertIntalioForm(request, response, companyId);
		} else if (editType.equals("insertCategory")) {
			insertCategory(request, response, companyId);
		} else if (editType.equals("updateCategory")) {
			updateCategory(request, response, companyId);
		} else if (editType.equals("insertURLForm")) {
			insertURLForm(request, response, companyId);
		} else if (editType.equals("updateForm")) {
			updateForm(request, response, companyId);
		}		
		response.setRenderParameter("action", "view");
	}

	private void removeCategory(ActionRequest request, ActionResponse response, Long companyId) {
		try {
			int categoryId = Integer.parseInt(request
					.getParameter("removeCategoryId"));
			int childCategories = servicesFacade.findChildCategories(categoryId, companyId).size();
			
			int childForms = servicesFacade.findChildForms(categoryId).size();
			LOG.info("Attempting to delete VeeraCategory id = "
					+ categoryId + ", child categories = "
					+ childCategories + ", child forms = " + childForms);

			if ((childCategories == 0) && (childForms == 0)) {
				LOG.info("Deleting category id = " + categoryId);
				int deleted = servicesFacade.removeCategoryByEntryAndCompanyId(categoryId, companyId);
				LOG.info("Delete count = " + deleted);
				if (deleted > 0) {
					response.setRenderParameter(UI_MESSAGE,
							"Kategorian poisto onnistui.");
					response.setRenderParameter(RESULTCODE, "0");
				} else {
					response.setRenderParameter(UI_MESSAGE,
							"Kategoriaa ei poistettu.");
					response.setRenderParameter(RESULTCODE, "1");
				}
			} else {
				response
						.setRenderParameter(UI_MESSAGE,
								"Kategoriaa, jossa on alikategorioita tai lomakkeita, ei voi poistaa.");
				response.setRenderParameter(RESULTCODE, "1");
			}
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Kategoriaa ei voitu poistaa: " + e);
			response.setRenderParameter(RESULTCODE, "1");
		} finally {
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}
	
	private void removeForm(ActionRequest request, ActionResponse response, Long companyId) {
		int deleted = 0;
		try {
			int entryId = Integer.parseInt(request.getParameter("formId"));
			deleted = servicesFacade.removeFormByEntryId(entryId);
			LOG.info("Delete count = " + deleted);
			if (deleted > 0) {
				response.setRenderParameter(UI_MESSAGE,
						"Lomakkeen poisto onnistui.");
				response.setRenderParameter(RESULTCODE, "0");
			} else {
				response.setRenderParameter(UI_MESSAGE,
						"Lomaketta ei poistettu.");
				response.setRenderParameter(RESULTCODE, "1");
			}
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Lomakkeen poisto ei onnistunut: " + e);
			response.setRenderParameter(RESULTCODE, "1");
		} finally {
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}

	private void insertIntalioForm(ActionRequest request, ActionResponse response, Long companyId) {
		VeeraFormImpl intForm = new VeeraFormImpl();
		try {
			intForm.setFolderId(Integer.valueOf(Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY))));
			intForm.setType(Integer.valueOf(2));
			intForm.setIdentity(VeeraFormImpl.identityTo64(request
					.getParameter("intalioList")));
			intForm.setCompanyId(companyId);
			servicesFacade.createForm(intForm);
			response.setRenderParameter(UI_MESSAGE,
					"Palvelun luonti onnistui.");
			response.setRenderParameter(RESULTCODE, "0");
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Palvelun luonti ei onnistunut: " + e);
			response.setRenderParameter(RESULTCODE, "1");
		} finally {
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}

	private void insertCategory(ActionRequest request, ActionResponse response, Long companyId) {
	
		VeeraCategoryImpl category = new VeeraCategoryImpl();
		try {
			category.setName(checkParameter(request, "categoryName"));
			category.setParent(Integer.valueOf(Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY))));
			category.setDescription(checkParameterAllowingEmpty(request,
					"categoryDescription"));
			category.setCompanyId(Long.valueOf(companyId));
			servicesFacade.createCategory(category);
//				VeeraCategory.createCategory(category);
			response.setRenderParameter(UI_MESSAGE,
					"Kategorian luonti onnistui.");
			response.setRenderParameter(RESULTCODE, "0");
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Kategorian luonti ei onnistunut: " + e);
			response.setRenderParameter(RESULTCODE, "1");
			response.setRenderParameter(EDIT_TYPE, SHOW_ADD_CATEGORY);
		} finally {
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}
	
	private void updateCategory(ActionRequest request, ActionResponse response, Long companyId) {

		VeeraCategoryImpl category = new VeeraCategoryImpl();
		try {
			category.setEntryId(Integer.valueOf(Integer.parseInt(request
					.getParameter("entryId"))));
			category.setName(checkParameter(request, "categoryName"));
			category.setParent(Integer.valueOf(Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY))));
			category.setDescription(checkParameterAllowingEmpty(request,
					"categoryDescription"));
			category.setCompanyId(Long.valueOf(companyId));
			servicesFacade.updateCategory(category);
			response.setRenderParameter(UI_MESSAGE,
					"Kategorian muutos onnistui.");
			response.setRenderParameter(RESULTCODE, "0");
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Kategorian muutos ei onnistunut: " + e);
			response.setRenderParameter(RESULTCODE, "1");
		} finally {
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}

	private void insertURLForm(ActionRequest request, ActionResponse response, Long companyId) {

		VeeraFormImpl form = new VeeraFormImpl();
		try {
			form.setFolderId(Integer.valueOf(Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY))));
			form.setType(Integer.valueOf(1));
			form.setIdentity(VeeraFormImpl.identityTo64(checkParameter(request,
					"formAddName")));
			form.setIdentity2(VeeraFormImpl
					.identityTo64(checkURL(checkParameter(request,
							"formAddURL"))));
			form.setDescription(checkParameterAllowingEmpty(request,
					"formAddDescription"));
			form.setCompanyId(companyId);
			servicesFacade.createForm(form);
			response.setRenderParameter(UI_MESSAGE,
					"Palvelun luonti onnistui.");
			response.setRenderParameter(RESULTCODE, "0");
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Palvelun luonti ei onnistunut: " + e);
			response.setRenderParameter(RESULTCODE, "1");
			response.setRenderParameter(EDIT_TYPE, SHOW_ADD_URL_FORM);
		} finally {
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}
	
	private void updateForm(ActionRequest request, ActionResponse response, Long companyId) {

		VeeraFormImpl form = new VeeraFormImpl();
		try {
			form.setFolderId(Integer.valueOf(Integer.parseInt(request
					.getParameter(CURRENT_CATEGORY))));
			form.setType(Integer.valueOf(Integer.parseInt(request
					.getParameter("formType"))));
			form.setEntryId(Integer.valueOf(Integer.parseInt(request
					.getParameter("entryId"))));
			form.setIdentity(VeeraFormImpl.identityTo64(checkParameter(request,
					"formAddName")));
			form.setCompanyId(companyId);

			int formType = Integer.parseInt(request
					.getParameter("formType"));
			if (formType != 2) {
				form.setIdentity2(VeeraFormImpl
						.identityTo64(checkURL(checkParameter(request,
								"formAddURL"))));
			}

			form.setDescription(checkParameterAllowingEmpty(request,
					"formAddDescription"));
			form.setHelpContent(checkParameterAllowingEmpty(request,
					"formAddHelpContent"));

			servicesFacade.updateForm(form);
			response.setRenderParameter(UI_MESSAGE,
					"Palvelun muokkaus onnistui.");
			response.setRenderParameter(RESULTCODE, "0");
		} catch (Exception e) {
			response.setRenderParameter(UI_MESSAGE,
					"Palvelun muokkaus ei onnistunut: " + e);
			response.setRenderParameter(RESULTCODE, "1");
		} finally {
			response.setRenderParameter(EDIT_TYPE, SHOW_RESULTS);
			response.setRenderParameter(CURRENT_CATEGORY, request
					.getParameter(CURRENT_CATEGORY));
		}
	}

	private List<TaskHolder<Task>> getTaskHolders(PortletRequest request) {
		String token = TokenUtil.getAuthenticationToken(request);

		List<Task> taskList = TaskUtil.getPIPATaskList(token);
		List<TaskHolder<Task>> tasks = new ArrayList<TaskHolder<Task>>();
		for (Task task : taskList) {
			String taskFormURL = URLUtil
					.getFormURLForTask(task, token, request);
			tasks.add(new TaskHolder<Task>(task, taskFormURL));
		}
		return tasks;
	}

	private String checkParameter(ActionRequest request, String parameter)
			throws Exception {
		String par = request.getParameter(parameter);
		if (par == null) {
			throw new Exception("Parameter is null = " + parameter);
		}
		par = par.trim();
		if (par.length() < 1) {
			throw new Exception("Parameter is empty = " + parameter);
		}
		if (par.length() > MAX_PARAM_LENGTH) {
			throw new Exception("Parameter is too long = " + parameter);
		}
		return par;
	}

	private String checkParameterAllowingEmpty(ActionRequest request,
			String parameter) throws Exception {
		String par = request.getParameter(parameter);
		if (par == null) {
			throw new Exception("Parameter is null = " + parameter);
		}
		par = par.trim();
		if (par.length() > MAX_PARAM_LENGTH) {
			throw new Exception("Parameter is too long = " + parameter);
		}
		return par;
	}

	private String checkURL(String argURL) throws MalformedURLException {
		String paramUrl = argURL;
		if (!paramUrl.startsWith("http://")) {
			paramUrl = "http://" + paramUrl;
		}		
		return new URL(paramUrl).toString();
	}
	
	private List<VeeraCategoryImpl> getPath(RenderRequest request, Integer rootFolderId, long companyId) {
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
			while (category.getEntryId() != rootFolderId) {
				path.add(category);
				category = servicesFacade.findCategoryByEntryAndCompanyId(currentFolderId, companyId);
			}
			path.add(category);
		}
		Collections.reverse(path);
		return path;
	}
}