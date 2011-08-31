package com.ixonos.koku.lok;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.support.ResourceBundleMessageSource;
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

import fi.koku.services.utility.log.v1.LogService;
import fi.koku.services.utility.log.v1.LogServicePortType;

//import com.ixonos.koku.kks.utils.Vakiot;

/**
 * Controller for log search (LOK). This implements LOK-3.
 * 
 * @author aspluma
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogSearchController {

  private static final Logger log = LoggerFactory.getLogger(LogSearchController.class);

  private static final String CRITERIA_RENDER_PARAM = "log-search-criteria";
  private CriteriaSerializer criteriaSerializer = new CriteriaSerializer();

  @Autowired
  private ResourceBundleMessageSource resourceBundle;

  // customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
    dateFormat.setLenient(false);
    CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
    binder.registerCustomEditor(Date.class, dateEditor);
  }

  // initialize form backing objects
  @ModelAttribute("logSearchCriteria")
  public LogSearchCriteria getCommandObject() {
    return new LogSearchCriteria();
  }

  @RenderMapping
  public String render(RenderRequest req, Model model) {
    return "menu";
  }

  private LogServicePortType getLogService() throws MalformedURLException {
    String uid = "marko";
    String pwd = "marko";
    String ep = "http://localhost:8080/log-service-0.0.1-SNAPSHOT/LogServiceBean?wsdl";

    URL wsdlLocation = new URL(ep);
    QName serviceName = new QName("http://services.koku.fi/utility/log/v1", "logService");
    LogService logService = new LogService(wsdlLocation, serviceName);

    LogServicePortType port = logService.getLogServiceSoap11Port();
    ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
    ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, pwd);
    return port;
  }

  // portlet render phase
  @RenderMapping(params = "action=searchLog")
  public String render(@RequestParam(value = "visited", required = false) String visited,
      @RequestParam(value = "pic", required = false) String pic, RenderRequest req, RenderResponse res, Model model) {
//TODO: yllä pic on aina null.
// Pitää muuttaa siten, että @RequestParam:lla otetaan koko searchCriteria
    
    log.info("log search render phase");
    log.info("req.pic=" + pic);

    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    LogSearchCriteria searchCriteria = null;
    if (req.getParameterValues(CRITERIA_RENDER_PARAM) != null) {
      searchCriteria = criteriaSerializer.getFromRenderParameter(req.getParameterValues(CRITERIA_RENDER_PARAM));
      model.addAttribute("entries", doSearchEntries(searchCriteria));
      model.addAttribute("searchParams", searchCriteria);
      model.addAttribute("visited", "---");
    }

    if (searchCriteria != null) {
      log.info("criteria: " + searchCriteria.getPic() + ", " + searchCriteria.getConcept() + ", "
          + searchCriteria.getFrom() + ", " + searchCriteria.getTo());
    } else {
      log.info("criteria: null");

      // Create new logsearchcriteria if there was pic in request params
      if (StringUtils.isNotBlank(pic)) {
        model.addAttribute("logSearchCriteria", new LogSearchCriteria(pic, "", null, null));
      }

    }
    return "search";
  }

  // portlet action phase
  @ActionMapping(params = "action=searchLog")
  public void doSearch(@ModelAttribute(value = "logSearchCriteria") LogSearchCriteria criteria, BindingResult result,
      ActionResponse response) {

    // TODO:
    // Hausta tallentuu tieto tapahtumalokin käsittelylokiin (tapahtumatietona
    // hakuehdot)

    // pass criteria to render phase
    response.setRenderParameter(CRITERIA_RENDER_PARAM, criteriaSerializer.getAsText(criteria));

    response.setRenderParameter("action", "searchLog");
  }

  private List<LogEntry> doSearchEntries(LogSearchCriteria searchCriteria) {
    List<LogEntry> r = null;

    // TEMPORARY solution:
    // show the results only if something has been written in the pic field
    if (searchCriteria.getPic() != null && searchCriteria.getPic().length() > 0) {

      /*
       * TODO: time stamp name of the user type of entry (? tapahtumatyyppi)
       * information that has been viewed or edited (käsitelty tieto
       * tapahtumakuvauksesta ja kohteesta) kutsuva palvelu
       */

      // TEMPORARY SOLUTION:
      // Create 5 lines of demo log entries
      LogDemoFactory factory = new LogDemoFactory();
      r = new ArrayList<LogEntry>();

      for (int i = 0; i < 5; i++) {
        r.add(factory.createLogEntry(i, LogDemoFactory.BASIC_LOG));
      }

    }
    return r;
  }

  /**
   * Helper class for serializing and deserializing LogSearchCriteria objects as
   * text.
   * 
   * NB: The class is expecting data to be valid since it's been already
   * validated by the controller so, input errors are not handled.
   * 
   * @author aspluma
   */
  private static class CriteriaSerializer {

    public String[] getAsText(LogSearchCriteria c) {
      String[] text = new String[]{};
      
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      
      if(c != null){
        text = new String[] { c.getPic(), c.getConcept(), c.getFrom() != null ? df.format(c.getFrom()) : "",
          c.getTo() != null ? df.format(c.getTo()) : "" };
      }
      return text;
    }

    
    //TODO: Is pic or some other parameter required for criteria?? Add error handling!
    public LogSearchCriteria getFromRenderParameter(String[] text) {
      SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
      String pic = null, concept = null;
      Date d1 = null, d2 = null;
      try {
        if(ArrayUtils.isNotEmpty(text)){
          if(StringUtils.isNotBlank(text[0])){
            pic = text[0]; // TODO: Add here some validation!!!
          }
          
          if(StringUtils.isNotBlank(text[1])){
            concept = text[1]; // TODO: What kind of input is accepted here?
          }
          
          if(StringUtils.isNotBlank(text[2])){
            d1 = df.parse(text[2]);
          }
          
          if(StringUtils.isNotBlank(text[3])){
            d2 = df.parse(text[3]);
          }
        }
       
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }
      
      return new LogSearchCriteria(pic, concept, d1, d2);
    }
  }

}
