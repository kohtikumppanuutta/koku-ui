package fi.arcusys.koku.palvelut.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import fi.arcusys.koku.palvelut.model.client.TaskHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.services.VeeraServicesFacade;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

@Resource(mappedName = "editController")
public class EditController extends AbstractController {
	public static final String EDIT_TYPE = "editType";
	public static final String SHOW_RESULTS = "showResults";
	public static final String SHOW_ADD_CATEGORY = "addCategory";
	public static final String SHOW_EDIT_CATEGORY = "editCategory";
	public static final String INSERT_CATEGORY = "insertCategory";
	public static final String UPDATE_CATEGORY = "updateCategory";
	public static final String CURRENT_CATEGORY = "categoryId";
	public static final String SHOW_ADD_INTALIO_FORM = "addIntalioForm";
	public static final String SHOW_ADD_URL_FORM = "addURLForm";
	public static final String SHOW_EDIT_FORM = "editForm";
	public static final String UPDATE_FORM = "updateForm";
	public static final String INSERT_INTALIO_FORM = "insertIntalioForm";
	public static final String INSERT_URL_FORM = "insertURLForm";
	public static final String ADMIN_REMOVE_CATEGORY_ACTION = "removeCategory";
	public static final String ADMIN_REMOVE_FORM_ACTION = "removeForm";
	public static final String UI_MESSAGE = "UIMessage";
	public static final String RESULTCODE = "resultCode";
	public static final String RESULTCODE_SUCCESS = "0";
	public static final String RESULTCODE_FAIL = "1";
	private static Log logger = LogFactory.getLog(EditController.class);

//	@Resource
	@Autowired
	private VeeraServicesFacade servicesFacade;
	
	

	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		long companyId = MigrationUtil.getCompanyId(request);

		String editType = request.getParameter("editType");

		logger.info("editType = " + editType);

		if ("addCategory".equals(editType)) {
			ModelAndView addCategory = new ModelAndView("editCategory");
			addCategory.addObject("categoryId", request
					.getParameter("categoryId"));
			addCategory.addObject("breadcrumb", getPath(request, Integer.parseInt(request
					.getParameter("categoryId")), companyId));
			return addCategory;
		}

		if ("editCategory".equals(editType)) {
			String catArg = request.getParameter("editCategoryId");
			try {
				int categoryId = Integer.parseInt(catArg);
				VeeraCategoryImpl category = servicesFacade.findCategoryByEntryAndCompanyId(categoryId, companyId);
				if (category == null) {
					logger.error("Invalid edit category id = " + categoryId);
					return new ModelAndView("view");
				}
				ModelAndView editCategory = new ModelAndView("editCategory");
				editCategory.addObject("editCategory", category);
				editCategory.addObject("categoryId", request
						.getParameter("categoryId"));
				editCategory.addObject("breadcrumb", getPath(request, Integer.parseInt(request
						.getParameter("categoryId")), companyId));
				return editCategory;
			} catch (Exception e) {
				logger.error("Invalid edit category parameter = " + catArg);
				return new ModelAndView("view");
			}
		}

		if ("addIntalioForm".equals(editType)) {
			ModelAndView addForm = new ModelAndView("addIntalioForm");
			addForm.addObject("formList", getTaskHolders(request));
			addForm.addObject("categoryId", request.getParameter("categoryId"));
			addForm.addObject("breadcrumb", getPath(request, Integer.parseInt(request
					.getParameter("categoryId")), companyId));
			return addForm;
		}

		if ("addURLForm".equals(editType)) {
			ModelAndView addForm = new ModelAndView("editForm");
			addForm.addObject("categoryId", request.getParameter("categoryId"));
			addForm.addObject("breadcrumb", getPath(request, Integer.parseInt(request
					.getParameter("categoryId")), companyId));
			return addForm;
		}

		if ("editForm".equals(editType)) {
			String formArg = request.getParameter("formId");
			try {
				int formId = Integer.parseInt(formArg);
				VeeraFormImpl form = servicesFacade.findFormByEntryId(formId);
				if (form == null) {
					logger.error("Invalid edit form id = " + formId);
					return new ModelAndView("view");
				}
				ModelAndView editForm = new ModelAndView("editForm");
				editForm.addObject("form", form);
				editForm.addObject("categoryId", request
						.getParameter("categoryId"));
				editForm.addObject("breadcrumb", getPath(request, Integer.parseInt(request
						.getParameter("categoryId")), companyId));
				return editForm;
			} catch (Exception e) {
				logger.error("Invalid edit form parameter = " + formArg);
				return new ModelAndView("view");
			}
		}

		if ("showResults".equals(editType)) {
			ModelAndView view = new ModelAndView("view");
			logger.info("UIMessage = " + request.getParameter("UIMessage"));
			view.addObject("UIMessage", request.getParameter("UIMessage"));
			view.addObject("resultCode", request.getParameter("resultCode"));
			view.addObject("categoryId", request.getParameter("categoryId"));
			view.addObject("breadcrumb", getPath(request, Integer.parseInt(request
					.getParameter("categoryId")), companyId));
			return view;
		}

		logger.error("No view defined for EDIT_TYPE = " + editType);
		return new ModelAndView("view");
	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		String editType = request.getParameter("editType");
		logger.info("ActionRequest editType parameter = " + editType);

		long companyId = MigrationUtil.getCompanyByUser(request);

		if ("removeCategory".equals(editType)) {
			try {
				int categoryId = Integer.parseInt(request
						.getParameter("removeCategoryId"));
				int childCategories = servicesFacade.findChildCategories(categoryId, companyId).size();
				
				int childForms = servicesFacade.findChildForms(categoryId).size();
				logger.info("Attempting to delete VeeraCategory id = "
						+ categoryId + ", child categories = "
						+ childCategories + ", child forms = " + childForms);

				if ((childCategories == 0) && (childForms == 0)) {
					logger.info("Deleting category id = " + categoryId);
					int deleted = servicesFacade.removeCategoryByEntryAndCompanyId(categoryId, companyId);
					logger.info("Delete count = " + deleted);
					if (deleted > 0) {
						response.setRenderParameter("UIMessage",
								"Kategorian poisto onnistui.");
						response.setRenderParameter("resultCode", "0");
					} else {
						response.setRenderParameter("UIMessage",
								"Kategoriaa ei poistettu.");
						response.setRenderParameter("resultCode", "1");
					}
				} else {
					response
							.setRenderParameter("UIMessage",
									"Kategoriaa, jossa on alikategorioita tai lomakkeita, ei voi poistaa.");
					response.setRenderParameter("resultCode", "1");
				}
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Kategoriaa ei voitu poistaa: " + e);
				response.setRenderParameter("resultCode", "1");
			} finally {
				response.setRenderParameter("editType", "showResults");
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}

		if ("removeForm".equals(editType)) {
			int deleted = 0;
			try {
				int entryId = Integer.parseInt(request.getParameter("formId"));
				deleted = servicesFacade.removeFormByEntryId(entryId);
				logger.info("Delete count = " + deleted);
				if (deleted > 0) {
					response.setRenderParameter("UIMessage",
							"Lomakkeen poisto onnistui.");
					response.setRenderParameter("resultCode", "0");
				} else {
					response.setRenderParameter("UIMessage",
							"Lomaketta ei poistettu.");
					response.setRenderParameter("resultCode", "1");
				}
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Lomakkeen poisto ei onnistunut: " + e);
				response.setRenderParameter("resultCode", "1");
			} finally {
				response.setRenderParameter("editType", "showResults");
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}

		if ("insertIntalioForm".equals(editType)) {
			VeeraFormImpl intForm = new VeeraFormImpl();
			try {
				intForm.setFolderId(Integer.valueOf(Integer.parseInt(request
						.getParameter("categoryId"))));
				intForm.setType(Integer.valueOf(2));
				intForm.setIdentity(VeeraFormImpl.identityTo64(request
						.getParameter("intalioList")));
				intForm.setCompanyId(companyId);
				servicesFacade.createForm(intForm);
				response.setRenderParameter("UIMessage",
						"Palvelun luonti onnistui.");
				response.setRenderParameter("resultCode", "0");
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Palvelun luonti ei onnistunut: " + e);
				response.setRenderParameter("resultCode", "1");
			} finally {
				response.setRenderParameter("editType", "showResults");
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}

		if ("insertCategory".equals(editType)) {
			VeeraCategoryImpl category = new VeeraCategoryImpl();
			try {
				category.setName(checkParameter(request, "categoryName"));
				category.setParent(Integer.valueOf(Integer.parseInt(request
						.getParameter("categoryId"))));
				category.setDescription(checkParameterAllowingEmpty(request,
						"categoryDescription"));
				category.setCompanyId(Long.valueOf(companyId));
				servicesFacade.createCategory(category);
//				VeeraCategory.createCategory(category);
				response.setRenderParameter("UIMessage",
						"Kategorian luonti onnistui.");
				response.setRenderParameter("resultCode", "0");
				response.setRenderParameter("editType", "showResults");
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Kategorian luonti ei onnistunut: " + e);
				response.setRenderParameter("resultCode", "1");
				response.setRenderParameter("editType", "addCategory");
			} finally {
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}

		if ("updateCategory".equals(editType)) {
			VeeraCategoryImpl category = new VeeraCategoryImpl();
			try {
				category.setEntryId(Integer.valueOf(Integer.parseInt(request
						.getParameter("entryId"))));
				category.setName(checkParameter(request, "categoryName"));
				category.setParent(Integer.valueOf(Integer.parseInt(request
						.getParameter("categoryId"))));
				category.setDescription(checkParameterAllowingEmpty(request,
						"categoryDescription"));
				category.setCompanyId(Long.valueOf(companyId));
				servicesFacade.updateCategory(category);
				response.setRenderParameter("UIMessage",
						"Kategorian muutos onnistui.");
				response.setRenderParameter("resultCode", "0");
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Kategorian muutos ei onnistunut: " + e);
				response.setRenderParameter("resultCode", "1");
			} finally {
				response.setRenderParameter("editType", "showResults");
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}

		if ("insertURLForm".equals(editType)) {
			VeeraFormImpl form = new VeeraFormImpl();
			try {
				form.setFolderId(Integer.valueOf(Integer.parseInt(request
						.getParameter("categoryId"))));
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
				response.setRenderParameter("UIMessage",
						"Palvelun luonti onnistui.");
				response.setRenderParameter("resultCode", "0");
				response.setRenderParameter("editType", "showResults");
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Palvelun luonti ei onnistunut: " + e);
				response.setRenderParameter("resultCode", "1");
				response.setRenderParameter("editType", "addURLForm");
			} finally {
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}

		if ("updateForm".equals(editType)) {
			VeeraFormImpl form = new VeeraFormImpl();
			try {
				form.setFolderId(Integer.valueOf(Integer.parseInt(request
						.getParameter("categoryId"))));
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
				response.setRenderParameter("UIMessage",
						"Palvelun muokkaus onnistui.");
				response.setRenderParameter("resultCode", "0");
			} catch (Exception e) {
				response.setRenderParameter("UIMessage",
						"Palvelun muokkaus ei onnistunut: " + e);
				response.setRenderParameter("resultCode", "1");
			} finally {
				response.setRenderParameter("editType", "showResults");
				response.setRenderParameter("categoryId", request
						.getParameter("categoryId"));
			}
		}
		response.setRenderParameter("action", "view");
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
		if (par.length() > 4000) {
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
		if (par.length() > 4000) {
			throw new Exception("Parameter is too long = " + parameter);
		}
		return par;
	}

	private String checkURL(String argURL) throws MalformedURLException {
		if (!argURL.startsWith("http://")) {
			argURL = "http://" + argURL;
		}
		URL url = new URL(argURL);
		return url.toString();
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