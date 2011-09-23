package fi.koku.lok;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.KoKuFaultException;
import fi.koku.services.utility.log.v1.LogService;
import fi.koku.services.utility.log.v1.LogServicePortType;

public class LogUtils {

  SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  private static final Logger log = LoggerFactory.getLogger(LogUtils.class);
  
  /**
   * Method for parsing a Date to a Calendar 
   * @param cal
   * @return
   * @throws KoKuFaultException
   */
  public Calendar parseGivenDate(Date date) throws KoKuFaultException {
    Calendar cal = Calendar.getInstance();
    
    if(date!=null){ // if it's null, return a null value
      if(date instanceof Date){
        cal.setTime(date);
      } else{
        throw new KoKuFaultException("wrong format of date");
      }
    }
    return cal;
  }
  /*
  if(searchCriteria.getFrom() == null){
    start = null;
  } else if{
    searchCriteria.getFrom() 
    try{
      start.setTime(searchCriteria.getFrom());
    }catch()
  }
  Calendar end = Calendar.getInstance();
  if(searchCriteria.getTo() == null){
    end = null;
  } else{
    end.setTime(searchCriteria.getTo());
  }
  */
 /**
  * The method returns a date X years from today.
  * @param years
  * @return String date
  */
  public String getDateString(int years) {
    
    Calendar time = Calendar.getInstance();
    time.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - years);

    String dateStr = df.format(time.getTime());
    
    return dateStr;
  }
  
  /**
   * The "real" LOK service.
   */
  public LogServicePortType getLogService() throws MalformedURLException {
    String uid = LogConstants.LOG_USERNAME;
    String pwd = LogConstants.LOG_PASSWORD;

    URL wsdlLocation = new URL(LogConstants.LOG_SERVICE);
    QName serviceName = new QName("http://services.koku.fi/utility/log/v1", "logService");
    LogService logService = new LogService(wsdlLocation, serviceName);

    log.debug("got logservice");
    
    LogServicePortType port = logService.getLogServiceSoap11Port();
    ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, uid);
    ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, pwd);
    
    return port;
  }
  
  /**
   * Method for transforming Date to XMLGregorianCalendar format
   * @param date
   * @return
   * @throws DatatypeConfigurationException
   */
  XMLGregorianCalendar getXMLGregorianDate(Date date) throws DatatypeConfigurationException {
    XMLGregorianCalendar xmlDate = null;
    
    if(date != null){
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      
      xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }
    
    return xmlDate;
  }
  
  /**
   * Method for transforming a date in XMLGregorianCalendar to String format
   * @param xmlcal
   * @return String date
   */
  String getDate(XMLGregorianCalendar xmlcal){
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.set(xmlcal.getYear(), xmlcal.getMonth(), xmlcal.getDay());
    date = cal.getTime();
    
    String dateStr = df.format(date);
    
    log.debug("getDate: "+dateStr);
    
    return dateStr;    
  }
  
  String getDate(Calendar cal){
    Date date = new Date();
  
    date = cal.getTime();
    
    String dateStr = df.format(date);
    
    log.debug("getDate: "+dateStr);
    
    return dateStr;    
  }
  
}
