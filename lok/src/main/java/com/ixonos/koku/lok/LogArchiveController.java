package com.ixonos.koku.lok;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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


/**
 * Controller for log archiving (LOK).
 * This implements LOK-2.
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogArchiveController {
  
  private static final String ARCHIVE_DATE_RENDER_PARAM = "log-archive";

  private static final Logger log = LoggerFactory.getLogger(LogArchiveController.class);
    
  private ArchiveSerializer archiveSerializer = new ArchiveSerializer();
  private SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  
  @Autowired
  private ResourceBundleMessageSource resourceBundle;
  
//customize form data binding
  @InitBinder
  public void initBinder(WebDataBinder binder) {
    dateFormat.setLenient(false);
    CustomDateEditor dateEditor = new CustomDateEditor(dateFormat, true);
    binder.registerCustomEditor(Date.class, dateEditor);
  }
  
  // initialize form backing objects
  @ModelAttribute("logArchiveDate")
  public LogArchiveDate getCommandObject() {
    return new LogArchiveDate();
  }
  
  /**
   * public String show(@ModelAttribute(value = "child") Person child,
      @RequestParam(value = "collection") String collection, RenderResponse response, Model model) {
   * @param req
   * @param res
   * @param model
   * @return
   */
  // portlet render phase
  @RenderMapping(params="action=archiveLog")
  public String render(RenderRequest req,  RenderResponse res, Model model) {
    //TODO: Pitäisi saada tällaiseen muotoon:
    // 28.8. ongelmia daten välityksessä jsp-sivulle, joten revertoidaan.
/*   public String render(RenderRequest req, @RequestParam(required=false, value="ARCHIVE_DATE_RENDER_PARAM") LogArchiveDate archivedate, RenderResponse res, Model model) {
//TODO: tähän sisään tulevan archivedaten pitäisi olla jo getDate()-muodossa!!!
//  if(archivedate != null){ 
    //     model.addAttribute("archivedate", archivedate.getDate());
  */
    
    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    LogArchiveDate archivedate = null;
    if(req.getParameterValues(ARCHIVE_DATE_RENDER_PARAM) != null) {
      archivedate = archiveSerializer.getFromRenderParameter(req.getParameterValues(ARCHIVE_DATE_RENDER_PARAM));
      model.addAttribute("archiveDate", archivedate.getDate());

    }    
    // TODO: Formatoidun päivämäärän välityksessä Controllerin ja JSP-sivun välillä oli
    // ongelmia, joten arkistointipäivämäärä näytetään nyt käyttäjälle hölmössä muodossa
    // esim. Wed Jul 29 00:00:00 EEST 2009.
    
    return "archive";
}

//portlet render phase
  @RenderMapping(params="action=startArchiveLog")
  public String renderStart(RenderRequest req, RenderResponse res, Model model) {
    //TODO: Muutettava tällaiseen muotoon:
 /* public String renderStart(RenderRequest req, @RequestParam("ARCHIVE_DATE_RENDER_PARAM") LogArchiveDate archivedate, RenderResponse res, Model model){
    if(archivedate != null){   
     log.debug("saatiin archiveDate render param");
     model.addAttribute("archivedate", archivedate); 
    }    
 */ 
    
    log.debug("log archive render phase: archiving started");
 
    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    LogArchiveDate archivedate = null;
    if(req.getParameterValues(ARCHIVE_DATE_RENDER_PARAM) != null) {
      archivedate = archiveSerializer.getFromRenderParameter(req.getParameterValues(ARCHIVE_DATE_RENDER_PARAM));
      model.addAttribute("archiveDate", archivedate.getDate());
    }
    

    return "archive2";
}
  
//portlet action phase
  @ActionMapping(params="action=archiveLog")
  public void doArchive(@ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate, BindingResult result,
      ActionResponse response) {
    log.debug("log archive action phase");
   
    // TODO:
    
    // 1) vahvistetaan käyttäjältä annettu päivä
    // 2) kopioi tapahtumalokista lokitiedot arkistolokiin 
    // 3) poista kopioidut tiedot tapahtumalokista
    // 4) taltioi käsittelylokiin tieto arkistoinnista
    // 
    // 5) ilmoita käyttäjälle, että arkistointi onnistui
    
    
    response.setRenderParameter(ARCHIVE_DATE_RENDER_PARAM, archiveSerializer.getAsText(logarchivedate));
    log.debug("render-parametriksi asetettu "+archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "archiveLog");
   
  }
  
  @ActionMapping(params="action=startArchiveLog")
  public void startArchive(@ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate, BindingResult result,
      ActionResponse response) {
    log.debug("log archive action phase: starting archiving");
   
    // TODO: ADD HERE THE ACTUAL ARCHIVING COMMAND
    
    response.setRenderParameter(ARCHIVE_DATE_RENDER_PARAM, archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "startArchiveLog");
  
  }
    
  private static class ArchiveSerializer{
    
    private SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
    
    public String getAsText(LogArchiveDate logarchivedate){
      String date = null;
    
      if(logarchivedate != null){
        date = logarchivedate.getDate() != null ? df.format(logarchivedate.getDate()) : ""; 
      
        log.debug("getAsText alkuperäinen archivedate: "+logarchivedate);
        log.debug("getAsText: formatoitu archivedate: "+date);
      } 
      
      return date;
    }
    
    public LogArchiveDate getFromRenderParameter(String[] text) {
   
      Date d1 = null;
      try {
        if(ArrayUtils.isNotEmpty(text) && StringUtils.isNotEmpty(text[0])){
          d1 = df.parse(text[0]);
        } 
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }
      
      return new LogArchiveDate(d1);
    }
  }
}