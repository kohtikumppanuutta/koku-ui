package com.ixonos.eservices.koku.kks;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.ixonos.eservices.koku.kks.utils.enums.KKSField;
import com.ixonos.eservices.koku.kks.utils.enums.UIField;

@Controller(value = "childController")
@RequestMapping(value = "VIEW")
public class ChildController {

	@Autowired
	@Qualifier("myKKSService")
	private KKSService service;

	private static Log log = LogFactory.getLog(ChildController.class);

	@ActionMapping(params = "myaction=routeToChild")
	public void addEntry(@ModelAttribute(value = "child") Child child,
			BindingResult bindingResult, ActionResponse response,
			SessionStatus sessionStatus) {
		log.debug("routeToChild");

		response.setRenderParameter("myaction", "viewChild");
		response.setRenderParameter("socialSecurityNumber",
				child.getSocialSecurityNumber());
		response.setRenderParameter("fields", UIField.ALL.toArray());
		sessionStatus.setComplete();
	}

	@RenderMapping(params = "myaction=viewChild")
	public String addEntry(@ModelAttribute(value = "child") Child child,
			@RequestParam("fields") String[] fields, RenderResponse response,
			Model model) {
		log.info("view child");
		model.addAttribute("child", child);
		model.addAttribute("entries",
				service.searchEntries(child, toKKSFields(fields)));
		return "child";
	}

	@ModelAttribute("child")
	public Child getChild(@RequestParam String socialSecurityNumber) {
		log.info("getChild");
		return service.getChild(socialSecurityNumber);
	}

	@ModelAttribute("entry")
	public KKSEntry getCommandObject() {
		log.debug("get entry command object");
		return new KKSEntry();
	}

	@ModelAttribute("search")
	public EntrySearch getSearchFilters() {
		return new EntrySearch();
	}

	private List<KKSField> toKKSFields(String[] fields) {
		List<KKSField> tmp = new ArrayList<KKSField>();

		for (String s : fields) {
			tmp.add(service.getField(s));
		}
		return tmp;
	}
}
