package fi.koku.lok;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

  SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  private static final Logger log = LoggerFactory.getLogger(LogUtils.class);
  
 /**
  * The method returns a date X years from today.
  * @param years
  * @return String date
  */
  public String getDateString(int years){
    
    Calendar time = Calendar.getInstance();
    time.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - years);

    String dateStr = df.format(time.getTime());
    
    return dateStr;
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
  
}
