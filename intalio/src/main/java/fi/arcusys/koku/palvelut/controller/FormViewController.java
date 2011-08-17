package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Resource;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.util.encoders.Base64;
import org.intalio.tempo.workflow.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import fi.arcusys.koku.palvelut.model.client.FormHolder;
import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.services.VeeraServicesFacade;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.TaskUtil;
import fi.arcusys.koku.palvelut.util.TokenUtil;
import fi.arcusys.koku.palvelut.util.URLUtil;

@Resource(mappedName = "formViewController")
public class FormViewController extends FormHolderController {
	public static final String VIEW_ACTION = "formview";
	public static final String TYPE_PARAMETER_NAME = "type";
	public static final String ENTRY_ID_PARAMETER_NAME = "entryId";
	public static final String ID_PARAMETER_NAME = "identity";
	public static final String ID2_PARAMETER_NAME = "identity2";
	public static final String HELP_CONTENT_MODEL_NAME = "helpContent";
	public static final String FORMHOLDER_MODEL_NAME = "formholder";
	private static Log log = LogFactory.getLog(FormViewController.class);
	
//	@Resource
	@Autowired
	private VeeraServicesFacade servicesFacade;
	
	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		FormHolder fh = null;
		long companyId = MigrationUtil.getCompanyId(request);
		String type = request.getParameter("type");
		String identity = new String(Base64.decode(request.getParameter(
				"identity").getBytes()));
		if (type.equalsIgnoreCase("1")) {
			String identity2 = new String(Base64.decode(request.getParameter(
					"identity2").getBytes()));
			fh = new FormHolder(identity, identity2);
		} else {
			log.debug("Getting FormHolder. identity = " + identity);
			fh = getFormHolderFromTask(request, identity);
		}

		int entryId = Integer.valueOf(Integer.parseInt(request
				.getParameter("entryId")));
		VeeraFormImpl form = servicesFacade.findFormByEntryId(entryId);
		String helpContent = form.getHelpContent();
		String helpContentWithoutNewlines = null;
		if (helpContent != null) {
			helpContentWithoutNewlines = helpContent.replace('\r', ' ')
					.replace('\n', ' ');
		}

		log.debug("helpContent at controller: " + helpContent);

		ModelAndView mav = new ModelAndView("formview");
		mav.addObject("formholder", fh);
		mav.addObject("helpContent", helpContentWithoutNewlines);
		mav.addObject("categoryId", request.getParameter("categoryId"));
		mav.addObject("breadcrumb", getPath(request, Integer.parseInt(request
				.getParameter("categoryId")), companyId));
		return mav;
	}

	public void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {
		log.info("action = " + request.getParameter("action"));
	}

//	private FormHolder getFormHolderFromTask(PortletRequest request,
//			String description) {
//		String token = TokenUtil.getAuthenticationToken(request);
//
//		List<Task> taskList = TaskUtil.getPIPATaskList(token);
//		for (Task task : taskList) {
//			if (task.getDescription().equals(description)) {
//				String taskFormURL = URLUtil.getFormURLForTask(task, token,
//						request);
//				try {
//					String userName = MigrationUtil.getUser(request);
//					Scanner scanner = new Scanner(description);
//					scanner.useDelimiter(" ");
//					String action = "";
//					while (scanner.hasNext()) {
//						action = action + scanner.next() + "_";
//					}
//					String actionString = action.substring(0, action.length());
//					String customerId = getPortletContext().getInitParameter(
//							"loggingCustomer");
//					String applicationId = getPortletContext()
//							.getInitParameter("loggingApplication");
//
//					String message = customerId + " " + applicationId + " "
//							+ userName + " Käyttäjä_avasi_lomakkeen_"
//							+ actionString;
//					log.warn(message);
//				} catch (Exception e) {
//				}
//				return new FormHolder(description, taskFormURL);
//			}
//		}
//		log.error("Didn't find any form!");
//
//		return null;
//	}
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