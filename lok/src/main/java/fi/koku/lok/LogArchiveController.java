package fi.koku.lok;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

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

import fi.koku.KoKuFaultException;
import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogArchivalParametersType;
import fi.koku.services.utility.log.v1.LogServiceFactory;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;

/**
 * Controller for log archiving (LOK). This implements LOK-2.
 * 
 * @author makinsu
 */
@Controller
@RequestMapping(value = "VIEW")
public class LogArchiveController {

  private static final Logger log = LoggerFactory.getLogger(LogArchiveController.class);

//Use log service
  private LogServicePortType logService;
  
  private ArchiveSerializer archiveSerializer = new ArchiveSerializer();
  SimpleDateFormat dateFormat = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  LogUtils lu = new LogUtils();
  
  public LogArchiveController(){
    LogServiceFactory logServiceFactory = new LogServiceFactory(
        LogConstants.LOG_SERVICE_USER_ID, LogConstants.LOG_SERVICE_PASSWORD,
        LogConstants.LOG_SERVICE_ENDPOINT);
    logService = logServiceFactory.getLogService();    
  }
  
  @Autowired
  private ResourceBundleMessageSource resourceBundle;

  // customize form data binding
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
   * 
   * @RequestParam(value = "collection") String collection, RenderResponse
   *                     response, Model model) {
   * @param req
   * @param res
   * @param model
   * @return
   */
  // portlet render phase
  @RenderMapping(params = "action=archiveLog")
  public String render(RenderRequest req, @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      RenderResponse res, Model model) {
//  public String render(RenderRequest req, @RequestParam("archiveDateStr") String dateStr,
//      RenderResponse res, Model model) {

    log.debug("action=archiveLog");

    try{
     // LogArchiveDate date = archiveSerializer.getFromRenderParameter(dateStr);
 
   // res.setProperty(key, resourceBundle.getMessage("koku.lok.archive.parsing.error", null, req.getLocale()));
  // tämä pätkä oli kommentoitu pois 23.9.
   //   if (dateStr != null && logarchivedate.getEndDate() != null) {
     if(logarchivedate!=null){
      log.debug("endDate palauttaa "+logarchivedate.getEndDate());
      
      log.debug("Formatointi alkaa");
      String archiveDateStr = dateFormat.format(logarchivedate.getEndDate());
      log.debug("Formatoinnin jälkeen: "+archiveDateStr);
      
      // this is needed so that the date can be easily formatted to YYYY-MM-dd format on the jsp
      model.addAttribute("archiveDateDate", logarchivedate.getEndDate());
      // this is the same in String format, used for logic on the jsp
      model.addAttribute("archiveDate", archiveDateStr);
    
   
      log.debug("modeliin lisätty archiveDateStr=" + archiveDateStr);
    } else{
      log.debug("logarchivedate == null");
    }
// ---
      }catch(KoKuFaultException e){
        log.error(e.getMessage(), e);
        //TODO: Lisää virheidenkäsittely
        // käyttäjälle näytetään virheviesti "koku.lok.archive.parsing.error"
        
      }
//    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    // set default archive time that is shown to the user at first. Default is 2
    // years ago.
    
      String defaultDateStr = lu.getDateString(2);
      model.addAttribute("defaultDate", defaultDateStr);  
      log.debug("modeliin lisätty defaultDateStr = " + defaultDateStr);
    
    
    return "archive";
  }

  // portlet render phase
  @RenderMapping(params = "action=startArchiveLog")
  public String renderStart(RenderRequest req, @ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      RenderResponse res, Model model) {

    log.debug("log archive render phase: archiving started");
    if (logarchivedate != null) {
      log.debug("archive end date: " + logarchivedate.getEndDate());
    } else{
      log.debug("action: logarchivedate == null!");
    }

   
//    res.setTitle(resourceBundle.getMessage("koku.lok.portlet.title", null, req.getLocale()));

    return "archive2";
  }

  // portlet action phase
  @ActionMapping(params = "action=archiveLog")
  public void doArchive(@ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate, BindingResult result,
      ActionResponse response) {

    log.debug("log archive action phase");

    /* TODO:
     1) vahvistetaan käyttäjältä annettu päivä TOTEUTETTU
    */
 
    String archivedate = archiveSerializer.getAsText(logarchivedate);
    if(archivedate == null){
      log.debug("archivedate on null!");
      //TODO: PITÄÄ LISÄTÄ KÄYTTÄJÄLLE ILMOITUS RUUDULLE VÄÄRÄSTÄ SYÖTTEESTÄ!
    }
    response.setRenderParameter("archiveDateStr", archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "archiveLog");

  }

  @ActionMapping(params = "action=startArchiveLog")
  public void startArchive(@ModelAttribute(value = "logArchiveDate") LogArchiveDate logarchivedate,
      BindingResult result, ActionResponse response) {
    log.debug("log archive action phase: starting archiving");

    try{
    
      LogArchivalParametersType archiveParametersType = new LogArchivalParametersType();
      
      Calendar end = Calendar.getInstance();
      end.setTime(logarchivedate.getEndDate());
      archiveParametersType.setEndDate(end);

      // call to log database
      AuditInfoType audit = new AuditInfoType();
      audit.setComponent("lok"); //FIXME
      audit.setUserId("luser");  // FIXME
      // call to log archive service
      logService.opArchiveLog(archiveParametersType, audit);
      
    /* TODO: TÄMÄ TULEE LOKSERVICEEN:
     *  2) kopioi tapahtumalokista lokitiedot arkistolokiin
        2.1) Annetulla aikavälillä ei ole yhtään arkistoitavaa lokitietoa
          -> Käyttäjälle ilmoitetaan UI:ssa (koku.lok.archive.noResults)
        2.2) arkistoloki ei vastaa tai kuittaa onnistunutta lokitietojen kopiointia
          -> lokitietoja ei poisteta tapahtumalokista
          -> käsittelylokiin tallennetaan virheviesti
          -> käyttäjälle ilmoitetaan virheestä UI:ssa (koku.lok.archive.error)
     3) poista kopioidut tiedot tapahtumalokista
     4) taltioi käsittelylokiin tieto arkistoinnista
    
     5) ilmoita käyttäjälle, että arkistointi onnistui TOTEUTETTU
     */
    
 
    }// TODO: lisää tähän catch sitä varten, että tulee virheet 2.1 tai 2.2
 catch (ServiceFault e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
     // TODO: Parempi virheenkäsittely
    response.setRenderParameter("archiveDateStr", archiveSerializer.getAsText(logarchivedate));
    response.setRenderParameter("action", "startArchiveLog");

  }

  private static class ArchiveSerializer {

    private SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);

    /**
     * Method formats the given Date into a String.
     * If the input is null or cannot be formatted, the method returns a null value.
     */
    public String getAsText(LogArchiveDate logarchivedate) {
      String date = null;

      if (logarchivedate != null) {
        date = logarchivedate.getEndDate() != null ? df.format(logarchivedate.getEndDate()) : "";

        log.debug("getAsText alkuperäinen archivedate: " + logarchivedate+","+logarchivedate.toString());
        log.debug("getAsText: formatoitu archivedate: " + date);
      }

      return date;
    }

    /**
     * Method for parsing the date parameter.
     * @param text
     * @return
     */
    public LogArchiveDate getFromRenderParameter(String text) {

      Date d1 = null;
      try {
        if (StringUtils.isNotEmpty(text)) {
          d1 = df.parse(text);
        }
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }

      return new LogArchiveDate(d1);
    }

    // TODO: tarvitaanko tätä?
    public LogArchiveDate getFromRenderParameter(String[] text) {

      Date d1 = null;
      try {
        if (ArrayUtils.isNotEmpty(text) && StringUtils.isNotEmpty(text[0])) {
          d1 = df.parse(text[0]);
        }
      } catch (ParseException e) {
        throw new IllegalArgumentException("error parsing date string", e);
      }

      return new LogArchiveDate(d1);
    }
  }
}