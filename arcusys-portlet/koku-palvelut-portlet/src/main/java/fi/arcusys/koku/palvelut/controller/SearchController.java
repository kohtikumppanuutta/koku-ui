/*
 * Copyright (c) 2007 Mermit Business Applications Oy $Id$
 * 
 * NOTICE: 
 * 1) Empty search parameter returns now all (there's NO validation for search param length)
 * 
 */
package fi.arcusys.koku.palvelut.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.arcusys.koku.palvelut.model.client.VeeraCategoryImpl;
import fi.arcusys.koku.palvelut.model.client.VeeraFormImpl;
import fi.arcusys.koku.palvelut.services.VeeraServicesFacade;
import fi.arcusys.koku.palvelut.util.MigrationUtil;
import fi.arcusys.koku.palvelut.util.VeeraFormComparator;

@Controller("searchController")
@RequestMapping(value = "VIEW")
public class SearchController {
	
	public static final String SEARCH_ACTION = "searchresults";
	public static final String SEARCH_RESULT_FORMS_ATTR = "forms";
	public static final String SEARCH_RESULT_MORE_THAN_LIMIT = "moreThanLimit";
	
	//User given search text
	public static final String SEARCH_TEXT_PARAM = "searchTextParam";

	private static final int MAX_RESULTS = 100;
	private static final Logger LOG = Logger.getLogger(SearchController.class);
	
//	@Resource
	@Autowired
	private VeeraServicesFacade servicesFacade;
	
	/*
	 * @see org.springframework.web.portlet.mvc.AbstractController#handleRenderRequestInternal(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	
	@SuppressWarnings("unchecked")
	@RenderMapping(params="action=search")
	public ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(SEARCH_ACTION);
		long companyId = MigrationUtil.getCompanyId(request);
		
		String param = request.getParameter(SEARCH_TEXT_PARAM);
		List<VeeraFormImpl> forms = servicesFacade.findFormsByDescription(param, MAX_RESULTS+1);
		
		if(forms!=null && forms.size()>0){
			//Results found
			Collections.sort(forms, new VeeraFormComparator());
			//Log form data if log level debug is enabled
			if(LOG.isDebugEnabled()){
				LOG.debug("VeeraForm.size="+forms.size());
				for (VeeraFormImpl veeraForm : forms) {
					LOG.debug(veeraForm.toString());	
				}
			}
			if(forms.size()>MAX_RESULTS){
				//Inform user that only 100 results are shown and suggest better search param
				request.setAttribute(SEARCH_RESULT_MORE_THAN_LIMIT, Integer.toString(MAX_RESULTS));
			}
		}else{
			//No results
			forms = new ArrayList<VeeraFormImpl>(0);//Empty List means that no results
		}
		request.setAttribute(SEARCH_RESULT_FORMS_ATTR, forms);		
		request.setAttribute(SEARCH_TEXT_PARAM, param);
		mav.addObject("breadcrumb", getPath(request, Integer.parseInt(request
				.getParameter("categoryId")), companyId));
		return mav;
	}
	
	@ActionMapping(params="action=search")
	public void handleActionRequestInternal( ActionRequest request, ActionResponse response ) throws Exception {
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
	
