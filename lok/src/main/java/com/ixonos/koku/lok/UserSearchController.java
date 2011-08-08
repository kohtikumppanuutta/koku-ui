package com.ixonos.koku.lok;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;


/**
 * Controller for user search (LOK).
 * This relates to LOK-3.
 * 
 * A simple controller that handles user search, listing and forwarding userid (ssn)
 * to next phase which is the actual log search
 * 
 * @author mikkope
 */
@Controller
@RequestMapping(value = "VIEW")
public class UserSearchController {
  
  private static Logger log = LoggerFactory.getLogger(UserSearchController.class);
  
  @Autowired
  @Qualifier(value = "lokDemoService")
  private LokDemoService lokDemoService;
  
  @Autowired
  private ResourceBundleMessageSource resourceBundle;
  
  @RenderMapping(params = "op=home")
  public String render(Model model){
    log.info("log search render phase");
    model.addAttribute("search", false);//This means that search was NOT done
    return "menu";
  }
  
  @RenderMapping(params = "op=searchUser")
  public String renderSearch(RenderRequest req, Model model){
    log.info("log search render phase");
    model.addAttribute("search", false);//This means that search was NOT done
    return "usersearch";
  }
  
  @ActionMapping(params = "op=searchUserWithParams")
  public void searchUserWithParams(ActionResponse response, @RequestParam(value = "fn", required = false) String fname, @RequestParam(value = "sn", required = false) String sname, @RequestParam(value = "ssn", required = false) String ssn, Model model){
    log.info("log search user action phase with op=searchUserWithParams");
    
    //Form sending required to use ActionURL and now there parameters are send forward to render method
    response.setRenderParameter("fn", fname);
    response.setRenderParameter("sn", sname);
    response.setRenderParameter("ssn", ssn);
    response.setRenderParameter("op", "searchUserParams");
  }
  
  @RenderMapping(params = "op=searchUserParams")
  public String renderParams(@RequestParam(value = "fn", required = false) String fname, @RequestParam(value = "sn", required = false) String sname, @RequestParam(value = "ssn", required = false) String ssn, RenderRequest req, RenderResponse res, Model model) {
    
    log.info("log search user render phase with op=searchUserParams"); 
    model.addAttribute("searchedUsers", lokDemoService.findUsers(ssn, null, fname, sname));
    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));
    model.addAttribute("search", true);//This means that search was done
    return "usersearch";
  }
 
  
}
