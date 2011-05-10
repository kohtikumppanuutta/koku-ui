package com.ixonos.eservices.koku.kks;

import java.util.List;

import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.eservices.koku.kks.mock.Child;
import com.ixonos.eservices.koku.kks.mock.KKSService;

@Controller(value = "guardianController")
@RequestMapping(value = "VIEW")
public class GuardianController {

	@Autowired
	@Qualifier("myKKSService")
	private KKSService service;

	private static Log log = LogFactory.getLog(GuardianController.class);

	@RenderMapping(params = "myaction=showChilds")
	public String showChilds(RenderResponse response, Model model) {
		log.info("showChilds");
		model.addAttribute("childs", getChilds());
		return "childs";
	}

	@ModelAttribute("childs")
	public List<Child> getChilds() {
		log.info("getChilds");
		return service.getChilds(null);
	}

}
