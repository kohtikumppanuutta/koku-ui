package com.ixonos.eservices.koku.kks;

import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.ixonos.eservices.koku.kks.mock.KKSService;

@Controller("viewController")
@RequestMapping(value = "VIEW")
public class ViewController {

	@Autowired
	@Qualifier("myKKSService")
	private KKSService service;

	private static Log log = LogFactory.getLog(ViewController.class);

	@RenderMapping
	public String render(RenderRequest req, Model model) {
		service.create("");
		return "choose";
	}

}
