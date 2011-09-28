package fi.koku.lok;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.portlet.PortletSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.portlet.filter.userinfo.UserInfo;

public class LogUtils {

  SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  private static final Logger log = LoggerFactory.getLogger(LogUtils.class);
  
  
  public Calendar dateToCalendar(Date d) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    return cal;
  }
 
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
   * Method for transforming Date to XMLGregorianCalendar format
   * @param date
   * @return
   * @throws DatatypeConfigurationException
   */
/*  XMLGregorianCalendar getXMLGregorianDate(Date date) throws DatatypeConfigurationException {
    XMLGregorianCalendar xmlDate = null;
    
    if(date != null){
      GregorianCalendar cal = new GregorianCalendar();
      cal.setTime(date);
      
      xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    }
    
    return xmlDate;
  }*/
  
  /**
   * Method for transforming a date in XMLGregorianCalendar to String format
   * @param xmlcal
   * @return String date
   */
/*  String getDate(XMLGregorianCalendar xmlcal){
    Date date = new Date();
    Calendar cal = Calendar.getInstance();
    cal.set(xmlcal.getYear(), xmlcal.getMonth(), xmlcal.getDay());
    date = cal.getTime();
    
    String dateStr = df.format(date);
    
    log.debug("getDate: "+dateStr);
    
    return dateStr;    
  }
  */
  
  String getDate(Calendar cal){
    Date date = new Date();
  
    date = cal.getTime();
    
    String dateStr = df.format(date);
    
    log.debug("getDate: "+dateStr);
    
    return dateStr;    
  }
  
  public static String getPicFromSession(PortletSession session) {
    UserInfo info = getUserInfoFromSession(session);
    if (info == null) {
      log.debug("cannot get user info from session");
      return "";
    }
    if(info.getPic()==null){
      log.error("cannot get pic from session");
    } else{
      log.debug("got pic from userinfo: "+info.getPic());
    }
    
    return info.getPic();
  }

  public static String getUsernameFromSession(PortletSession session){
    UserInfo info = getUserInfoFromSession(session);
    if (info == null) {
      log.debug("cannot get user info from session");
      return "";
    }
    if(info.getUid()==null){
      log.error("cannot get uid from session");
    } else{
      log.debug("got uid from userinfo: "+info.getUid());
    }
    
    return info.getUid();
  }
  
  
  public static UserInfo getUserInfoFromSession(PortletSession session) {
    return (UserInfo) session.getAttribute(UserInfo.KEY_USER_INFO);
  }
  
}
