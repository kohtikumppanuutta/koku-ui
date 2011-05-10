package com.ixonos.eservices.koku.kks;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.eservices.koku.kks.mock.Child;
import com.ixonos.eservices.koku.kks.mock.EntrySearch;
import com.ixonos.eservices.koku.kks.mock.KKSEntry;
import com.ixonos.eservices.koku.kks.mock.KKSService;
import com.ixonos.eservices.koku.kks.utils.KKSFieldEditor;
import com.ixonos.eservices.koku.kks.utils.enums.KKSField;
import com.ixonos.eservices.koku.kks.utils.enums.UIField;

@Controller(value = "addKKSEntryController")
@RequestMapping(value = "VIEW")
public class AddKKSEntryController {

	@Autowired
	@Qualifier("myKKSService")
	private KKSService service;

	private static Log log = LogFactory.getLog(AddKKSEntryController.class);

	@RenderMapping(params = "myaction=addEntryForm")
	public String createEntry(@ModelAttribute(value = "child") Child child,
			RenderResponse response, Model model) {
		log.debug("add entry");
		model.addAttribute("child", child);
		return "add_entry";
	}

	@ActionMapping(params = "myaction=addEntry")
	public void addEntry(@ModelAttribute(value = "entry") KKSEntry entry,
			@ModelAttribute(value = "child") Child child,
			BindingResult bindingResult, ActionResponse response,
			SessionStatus sessionStatus) {
		log.debug("add entry");
		entry.setChild(child);
		service.addEntry(entry);
		entry.setChild(child);
		response.setRenderParameter("myaction", "viewChild");
		response.setRenderParameter("socialSecurityNumber",
				child.getSocialSecurityNumber());
		response.setRenderParameter("fields", UIField.ALL.toArray());
		sessionStatus.setComplete();
	}

	@InitBinder("entry")
	public void initBinder(WebDataBinder binder) {
		log.debug("init binder");
		binder.registerCustomEditor(KKSField.class, new KKSFieldEditor(service));
	}

	@ModelAttribute("entry")
	public KKSEntry getCommandObject() {
		log.debug("get entry command object");
		return new KKSEntry();
	}

	@ModelAttribute("child")
	public Child getChild(@RequestParam String socialSecurityNumber) {
		log.debug("getChild");
		return service.getChild(socialSecurityNumber);
	}

	@ModelAttribute("search")
	public EntrySearch getSearchCommandObject() {
		log.debug("get search command object in add controller");
		return new EntrySearch();
	}
}
