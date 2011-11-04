/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.pyh.ui.common;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.calendar.CalendarUtil;
import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogServiceFactory;
import fi.koku.services.utility.log.v1.LogServicePortType;

/**
 * This class provides logging services for using the LOK component of KoKu.
 * 
 * Used as a singleton instance.
 * 
 * @author hurulmi
 *
 */

public class Log {

  private static final Logger logger = LoggerFactory.getLogger(Log.class);
  
  public static final String CLIENT_ID = "pyh";
  public static final String UPDATE = "update";
  public static final String SEND = "send";
  public static final String REMOVE = "remove";
  
  private LogServicePortType logServicePortType;
  private static Log log = new Log();
  
  private Log() {
    logServicePortType = getLogService();
  }
  
  public static Log getInstance() {
    return log;
  }
  
  // userId == user's PIC (required as parameter)
  // customerPic == child's PIC (not required as parameter, can be empty string)
  public void update(String userId, String customerPic, String dataType, String message) {
    log(Log.UPDATE, userId, customerPic, dataType, message);
  }
  
  public void send(String userId, String customerPic, String dataType, String message) {
    log(Log.SEND, userId, customerPic, dataType, message);
  }
  
  public void remove(String userId, String customerPic, String dataType, String message) {
    log(Log.REMOVE, userId, customerPic, dataType, message);
  }
  
  private LogServicePortType getLogService() {
    
    LogServiceFactory logServiceFactory = new LogServiceFactory(PyhConstants.LOK_SERVICE_USER_ID, PyhConstants.LOK_SERVICE_PASSWORD, PyhConstants.LOK_SERVICE_ENDPOINT);
    return logServiceFactory.getLogService();
  }
  
  /**
   * Creates AuditInfo for log web service call.
   * 
   */
  private AuditInfoType getLogAuditInfo(String userId) {
    
    AuditInfoType logAuditInfoType = new fi.koku.services.utility.log.v1.AuditInfoType();
    logAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    logAuditInfoType.setUserId(userId);
    
    return logAuditInfoType;
  }
  
  /**
   * Calls the actual log web service.
   * 
   */
  private void log(String operation, String userId, String customerPic, String dataType, String message) {
    
    try {
      LogEntryType logEntryType = new LogEntryType();
      logEntryType.setClientSystemId(Log.CLIENT_ID);
      logEntryType.setCustomerPic(customerPic);
      logEntryType.setDataItemId(Log.CLIENT_ID);
      logEntryType.setDataItemType(dataType);
      logEntryType.setMessage(message);
      logEntryType.setOperation(operation);
      logEntryType.setTimestamp(CalendarUtil.getXmlDateTime(new Date()));
      logEntryType.setUserPic(userId);
      
      LogEntriesType entries = new LogEntriesType();
      entries.getLogEntry().add(logEntryType);
      logServicePortType.opLog(entries, getLogAuditInfo(userId));
      
    } catch (fi.koku.services.utility.log.v1.ServiceFault fault) {
      // if log operation fails, stacktrace will be logged in server log
      // PYH still operates normally even if the logging fails
      logger.error("LOK service failed to log operation " + operation + " for data type " + dataType, fault);
    }
  }
}
