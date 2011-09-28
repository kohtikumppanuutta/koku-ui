package fi.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogServiceFactory;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

/**
 * Controller for the main view of Lok portlet.
 * 
 * @author makinsu
 */
@Controller(value = "logController")
@RequestMapping(value = "VIEW")
public class LogController {

  private static final Logger log = LoggerFactory.getLogger(LogSearchController.class);
  LogUtils lu = new LogUtils();

 
  // start page
 // @RenderMapping
//  public String render(RenderRequest req, Model model) {
 
  @RenderMapping
  public String render(PortletSession session,  Model model) {
    log.debug("Main Log controller");
    
/*    String userPic = ""; // hard-coded pic for testing
    
    UserInfo userInfo = (UserInfo)session.getAttribute(UserInfo.KEY_USER_INFO);
    if (userInfo != null) {
      userPic = userInfo.getPic();
      log.info("Got PIC from session. userPic = " + userPic);
      
    } else {
      // TODO: mitä tehdään kun käyttäjää ei voida tunnistaa?
      log.error("ERROR: UserInfo returns no PIC!");
    }
    
    model.addAttribute("user", userPic);
  */
    model.addAttribute("user", lu.getPicFromSession(session));
    // this is used for selecting which part of the page to show
    model.addAttribute("useruid", lu.getUsernameFromSession(session));
 
    
    return "menu";
  }

}
