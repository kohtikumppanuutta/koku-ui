package com.ixonos.eservices.koku.kks;

import javax.portlet.ActionResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.ixonos.eservices.koku.kks.mock.Child;
import com.ixonos.eservices.koku.kks.mock.EntrySearch;
import com.ixonos.eservices.koku.kks.mock.KKSService;
import com.ixonos.eservices.koku.kks.utils.KKSFieldEditor;
import com.ixonos.eservices.koku.kks.utils.enums.KKSField;

@Controller(value = "searchEntriesController")
@RequestMapping(value = "VIEW")
public class SearchEntriesController {

	@Autowired
	@Qualifier("myKKSService")
	private KKSService service;

	private static Log log = LogFactory.getLog(SearchEntriesController.class);

	@ActionMapping(params = "myaction=searchEntries")
	public void search(@ModelAttribute(value = "search") EntrySearch search,
			@ModelAttribute(value = "child") Child child,
			BindingResult bindingResult, ActionResponse response,
			SessionStatus sessionStatus) {

		response.setRenderParameter("myaction", "viewChild");
		response.setRenderParameter("socialSecurityNumber",
				child.getSocialSecurityNumber());
		response.setRenderParameter("fields", toArray(search));
		search.clear();
		sessionStatus.setComplete();
	}

	@InitBinder("search")
	public void initBinder(WebDataBinder binder) {
		log.debug("init binder");
		binder.registerCustomEditor(KKSField.class, new KKSFieldEditor(service));
	}

	@ModelAttribute("search")
	public EntrySearch getCommandObject() {
		log.debug("get entry command object");
		return new EntrySearch();
	}

	@ModelAttribute("child")
	public Child getChild(@RequestParam String socialSecurityNumber) {
		log.debug("getChild");
		return service.getChild(socialSecurityNumber);
	}

	private String[] toArray(EntrySearch search) {
		String[] tmp = null;

		if (search.getFields() == null || search.getFields().size() == 0) {
			tmp = new String[1];
			tmp[0] = search.getMainField().getId();
		} else {

			boolean containsMain = search.getFields().contains(
					search.getMainField());

			int arraySize = containsMain ? search.getFields().size() : search
					.getFields().size() + 1;

			tmp = new String[arraySize];

			if (!containsMain) {
				tmp[0] = search.getMainField().getId();
			}
			for (int i = 0; i < search.getFields().size(); i++) {
				tmp[i] = search.getFields().get((i)).getId();
			}

			if (!containsMain) {
				tmp[arraySize - 1] = search.getMainField().getId();
			}
		}
		return tmp;
	}
}
