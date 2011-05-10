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
import com.ixonos.eservices.koku.kks.mock.KKSService;

@Controller(value = "specialistController")
@RequestMapping(value = "VIEW")
public class SpecialistController {

	@Autowired
	@Qualifier("myKKSService")
	private KKSService service;

	private static Log log = LogFactory.getLog(SpecialistController.class);

	@RenderMapping(params = "myaction=showSpecialist")
	public String showSpecialist(RenderResponse response,
			@RequestParam("childs") String[] childs, Model model) {
		log.info("showSpecialist");
		model.addAttribute("childs", toChilds(childs));
		return "search";
	}

	@ActionMapping(params = "myaction=searchChild")
	public void search(@ModelAttribute(value = "search") Child child,
			BindingResult bindingResult, ActionResponse response,
			SessionStatus sessionStatus) {
		log.debug("search child");

		response.setRenderParameter("myaction", "showSpecialist");
		response.setRenderParameter("childs",
				toArray(service.searchChilds(child)));
		sessionStatus.setComplete();
	}

	@ModelAttribute("search")
	public Child getCommandObject() {
		log.debug("get entry command object");
		return new Child();
	}

	@ModelAttribute("search")
	public EntrySearch getSearchCommandObject() {
		log.debug("get search command object in add controller");
		return new EntrySearch();
	}

	private String[] toArray(List<Child> childs) {
		String[] tmp = null;

		if (childs.isEmpty()) {
			tmp = new String[] { "" };
		} else {
			tmp = new String[childs.size()];
		}

		for (int i = 0; i < childs.size(); i++) {
			tmp[i] = childs.get(i).getSocialSecurityNumber();
		}
		return tmp;
	}

	private List<Child> toChilds(String[] childIds) {
		List<Child> tmp = new ArrayList<Child>();

		if (childIds != null) {
			for (String s : childIds) {
				if (!"".equals(s)) {
					tmp.add(service.getChild(s));
				}
			}
		}
		return tmp;
	}

}
