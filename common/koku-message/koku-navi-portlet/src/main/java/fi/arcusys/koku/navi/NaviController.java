package fi.arcusys.koku.navi;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handle the main message page
 * 
 * @author Jinhua Chen
 * 
 */
@Controller("naviController")
@RequestMapping(value = "VIEW")
public class NaviController {

	// --maps the incoming portlet request to this method
	@RenderMapping
	public String home(RenderRequest request, RenderResponse response,
			ModelMap modelmap) {
		
		return "navi";
	}

	// -- @ModelAttribute here works as the referenceData method
	@ModelAttribute(value = "loginStatus")
	public String model(RenderRequest request) {
		String user = request.getRemoteUser();
		if(user != null)
			return "VALID";
		else
			return "INVALID";
	}
	
	@RenderMapping(params = "myaction=showNavi")
	public String showNavi(@RequestParam(value = "naviType", required = false) String naviType,
			RenderRequest request, RenderResponse response, ModelMap modelmap) {
		modelmap.addAttribute("naviType", naviType);
		System.out.println("show navi home page");
		
		return "navi";
	}

}
