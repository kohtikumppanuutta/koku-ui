package fi.koku.lok;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.calendar.CalendarUtil;
import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.services.entity.person.v1.Person;
import fi.koku.services.entity.person.v1.PersonConstants;
import fi.koku.services.entity.person.v1.PersonService;
import fi.koku.services.utility.log.v1.LogEntryType;

/**
 * Helper class for LOK portlet controllers.
 * 
 * @author makinsu
 * 
 */
public class LogUtils {

  private SimpleDateFormat df = new SimpleDateFormat(LogConstants.DATE_FORMAT);
  private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

  private Calendar dateToCalendar(Date d) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);

    return cal;
  }

  /**
   * Helper method that checks if a given date is before today.
   * @param date
   * @return
   */
  public boolean isBeforeToday(Date date) {

    boolean isBefore = false;
    
    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);

    Calendar newDate = dateToCalendar(date);

    newDate.set(Calendar.HOUR_OF_DAY, 0);
    newDate.set(Calendar.MINUTE, 0);
    newDate.set(Calendar.SECOND, 0);
    newDate.set(Calendar.MILLISECOND, 0);

    if (newDate.before(today)) {
      isBefore = true;
    } 
    
    return isBefore;
  }

  /**
   * Helper method that checks if the query parameters given by the user are
   * null. If parsing of the dates has not succeeded, the parsing method returns
   * null. The method also checks that the given start date is not after the end
   * date.
   * 
   * @param criteria
   * @param logtype
   * @return error string array
   */
  public String[] checkInputParameters(LogSearchCriteria criteria, String logtype) {
    String[] error = new String[4];

    if (criteria.getFrom() == null) {
      error[0] = "koku.lok.search.null.from";
    }
    if (criteria.getTo() == null) {
      error[1] = "koku.lok.search.null.to";
    }

    if (LogConstants.LOG_NORMAL.equalsIgnoreCase(logtype)) {
      if (criteria.getConcept() == null || criteria.getConcept().isEmpty()) {
        error[2] = "koku.lok.search.null.concept";
      }
    }

    // check that start date is before end date
    if (criteria.getFrom() != null && criteria.getTo() != null) {
      Calendar from = dateToCalendar(criteria.getFrom());
      Calendar to = dateToCalendar(criteria.getTo());

      if (to.before(from)) {
        error[3] = "koku.lok.error.start.after.end";
      }
    }

    return error;
  }

  public LogEntryType toWsFromAdminType(AdminLogEntry entry) {

    LogEntryType entryType = new LogEntryType();
    entryType.setCustomerPic(entry.getCustomer());
    entryType.setUserPic(entry.getUser());
    entryType.setOperation(entry.getOperation());
    entryType.setMessage(entry.getMessage());
    entryType.setTimestamp(CalendarUtil.getXmlDateTime(entry.getTimestamp()));

    // AdminLogEntry will be written to admin log, set the log type here
    entryType.setClientSystemId(LogConstants.LOG_WRITER_LOG);
    return entryType;
  }

  /**
   * The method returns a date X years from today.
   * 
   * @param years
   * @return String date
   */
  public String getDateString(int years) {

    Calendar time = Calendar.getInstance();
    time.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - years);

    String dateStr = df.format(time.getTime());

    return dateStr;
  }

  String getDate(Calendar cal) {
    Date date = new Date();

    date = cal.getTime();

    String dateStr = df.format(date);

    return dateStr;
  }

  public static String getPicFromSession(PortletSession session) {
    UserInfo info = getUserInfoFromSession(session);
    if (info == null) {
      log.error("cannot get user info from session");
      return "";
    }
    if (info.getPic() == null) {
      log.error("cannot get pic from session");
    } else {
      log.debug("got pic from userinfo: " + info.getPic());
    }

    return info.getPic();
  }

  public static String getUsernameFromSession(PortletSession session) {
    UserInfo info = getUserInfoFromSession(session);
    if (info == null) {
      log.error("cannot get user info from session");
      return "";
    }
    if (info.getUid() == null) {
      log.error("cannot get uid from session");
    } else {
      log.debug("got uid from userinfo: " + info.getUid());
    }

    return info.getUid();
  }

  public static UserInfo getUserInfoFromSession(PortletSession session) {
    return (UserInfo) session.getAttribute(UserInfo.KEY_USER_INFO);
  }

  /**
   * Helper method that changes the pic value in every entry to the user's name,
   * read from PersonService.
   * 
   * @param entries
   * @return
   */
  public void changePicsToNames(List<LogEntry> entries, String portletUserPic, PersonService personService) {
    String pic = null;
    List<Person> list = null;
    List<String> picList = new ArrayList<String>();

    // collect all different pics found in entries in a local list
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
      LogEntry entry = (LogEntry) iter.next();
      pic = entry.getUser();
      if (!picList.contains(pic)) {
        picList.add(pic);
      }
    }

    try {
      log.info("call the Person service");
      // call the Person service to get the persons
      list = personService.getPersonsByPics(picList, PersonConstants.PERSON_SERVICE_DOMAIN_OFFICER, portletUserPic,
          LogConstants.COMPONENT_LOK);
    } catch (Exception e) {
      log.error("Person service threw an exception " + e.getMessage());
    }

    // Replace the pics in entries with the names we got from Person service
    Iterator<LogEntry> it = entries.iterator();
    while (it.hasNext()) {
      LogEntry entry = (LogEntry) it.next();
      String userpic = entry.getUser();

      changeEntry(entry, picList, list, userpic);
    }
  } 
  
  
  /**
   * Helper method that changes the pic value in every admin entry to the user's
   * name, read from PersonService.
   * 
   * @param entries
   * @return
   */
  public void changePicsToNamesAdmin(List<AdminLogEntry> entries, String portletUserPic, PersonService personService) {
    String pic = null;
    List<Person> list = null;
    List<String> picList = new ArrayList<String>();

    // collect all different pics found in entries in a local list
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
      AdminLogEntry entry = (AdminLogEntry) iter.next();
      pic = entry.getUser();
      if (!picList.contains(pic)) {
        picList.add(pic);
      }
    } 
     
    try {
      log.info("call the Person service");
      // call the Person service to get the persons
      list = personService.getPersonsByPics(picList, PersonConstants.PERSON_SERVICE_DOMAIN_OFFICER, portletUserPic,
          LogConstants.COMPONENT_LOK);
    } catch (Exception e) {
      log.error("Person service threw an exception " + e.getMessage());
    }

    // Replace the pics in entries with the names we got from Person service
    Iterator<AdminLogEntry> it = entries.iterator();
    while (it.hasNext()) {
      AdminLogEntry entry = (AdminLogEntry) it.next();
      String userpic = entry.getUser();

      changeAdminEntry(entry, picList, list, userpic);   
      }
    }
  
 
  /**
   * Helper method for replacing the pic in the entry with the name we got from
   * the Person service
   * 
   * @param entries
   * @param picList
   * @param list
   */
  private void changeAdminEntry(AdminLogEntry entry, List<String> picList, List<Person> list, String userpic){
   Person person = null;  
 
   for (int i = 0; i < picList.size(); i++) {
     if(picList.get(i).equalsIgnoreCase(userpic)){
       person = list.get(i);
       if(person==null || person.getFname()==null || person.getSname()==null){
         log.info("No name found in personservice for pic " + userpic);
         // no name was found so keep the original pic in the entry!
       } else{
         entry.setUser(person.getFname() + " "+person.getSname());
       }
     }
   }
  }
  
  /**
   * Helper method for replacing the pic in the entry with the name we got from
   * the Person service
   * 
   * @param entries
   * @param picList
   * @param list
   */
  private void changeEntry(LogEntry entry, List<String> picList, List<Person> list, String userpic){
   Person person = null;  
 
   for (int i = 0; i < picList.size(); i++) {
     if(picList.get(i).equalsIgnoreCase(userpic)){
       person = list.get(i);
       if(person==null || person.getFname()==null || person.getSname()==null){
         log.info("No name found in personservice for pic " + userpic);
         // no name was found so keep the original pic in the entry!
       } else{
         entry.setUser(person.getFname() + " "+person.getSname());
       }
     }
   }
  }
  
 
}
