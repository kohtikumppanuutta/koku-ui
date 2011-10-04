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
    log.debug("dateToCalendar date = "+d.getTime());
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    log.debug("cal = "+cal.getTime());
    return cal;
  }
 
  public boolean isBeforeToday(Date date){
    
    Calendar today = Calendar.getInstance();
   log.debug("today: "+today.getTime());
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    log.debug("after set today: "+today.getTime());
    //   todayDate.set(Calendar.DATE, today.get(Calendar.DATE));
 //   todayDate.set(Calendar.MONTH, today.get(Calendar.MONTH));
 //   todayDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
    Calendar newDate = dateToCalendar(date);
    log.debug("newdate: "+newDate.getTime());
    newDate.set(Calendar.HOUR_OF_DAY, 0);
    newDate.set(Calendar.MINUTE, 0);
    newDate.set(Calendar.SECOND, 0);
    newDate.set(Calendar.MILLISECOND, 0);
    log.debug("after set new date: "+newDate.getTime());
 /*   newDate.set(Calendar.DATE, date.get(Calendar.DATE));
    newDate.set(Calendar.MONTH, today.get(Calendar.MONTH));
    newDate.set(Calendar.YEAR, today.get(Calendar.YEAR));
   */ 
    if(newDate.before(today)){
      return true;
    } else{
      return false;
    }
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
