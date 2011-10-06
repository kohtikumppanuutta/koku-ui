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

import fi.koku.services.entity.authorizationinfo.v1.AuthorizationInfoService;
import fi.koku.services.entity.authorizationinfo.v1.impl.AuthorizationInfoServiceDummyImpl;
import fi.koku.services.entity.authorizationinfo.v1.model.Group;
import fi.koku.services.entity.authorizationinfo.v1.model.OrgUnit;
import fi.koku.services.entity.authorizationinfo.v1.model.Registry;
import fi.koku.services.entity.authorizationinfo.v1.model.Role;
import fi.koku.services.entity.authorizationinfo.v1.model.User;
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
  private LogUtils lu = new LogUtils();
 
  private AuthorizationInfoService authorizationInfoService;
  
  public LogController() {
    authorizationInfoService = new AuthorizationInfoServiceDummyImpl();
  }
 
 
  @RenderMapping
  public String render(PortletSession session,  Model model) {
    log.debug("Main Log controller");  
    String currentRole = null;
    
    model.addAttribute("user", LogUtils.getPicFromSession(session));
    
    // this is used for selecting which part of the page to show
    for(Role r : authorizationInfoService.getUsersRoles("lok", LogUtils.getPicFromSession(session))) {
      log.debug(r.getId());
      if(r.getId().startsWith("ROLE_LOK_")) { // FIXME
        model.addAttribute("userRole", r.getId());
        currentRole = r.getId();
        log.debug("added user role "+r.getId());
      }
    }   
    
    log.debug(LogConstants.ROLE_LOK_LOG_ADMIN.getName());
    log.debug("current role: "+currentRole);

    return "menu";

    
  }

}
