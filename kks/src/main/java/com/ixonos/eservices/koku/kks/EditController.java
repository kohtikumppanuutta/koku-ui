package com.ixonos.eservices.koku.kks;

import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller("editController")
@RequestMapping("EDIT")
public class EditController {

	private static Log log = LogFactory.getLog(EditController.class);
	
	@RenderMapping
	public String showEditPropertiesForm(Model model, RenderRequest req) {
		return "choose"; 
	}

	@ActionMapping
	public void editProperties(){
		//Do nothing pr
	}
	
	

}
