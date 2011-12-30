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
  
  /**
   * Only one Log instance is allowed.
   */
  private Log() {
    logServicePortType = getLogService();
  }
  
  /**
   * @return - Log instance
   */
  public static Log getInstance() {
    return log;
  }
  
  /**
   * Log an update operation.
   * 
   * @param userId - current user's PIC
   * @param customerPic - child's PIC (not required, can be empty string)
   * @param dataType - data type identifier, for example "pyh.family.community"
   * @param message - log message
   */
  public void update(String userId, String customerPic, String dataType, String message) {
    log(Log.UPDATE, userId, customerPic, dataType, message);
  }
  
  /**
   * Log a send operation.
   * 
   * @param userId - current user's PIC
   * @param customerPic - child's PIC (not required, can be empty string)
   * @param dataType - data type identifier, for example "pyh.family.community"
   * @param message - log message
   */
  public void send(String userId, String customerPic, String dataType, String message) {
    log(Log.SEND, userId, customerPic, dataType, message);
  }
  
  /**
   * Log a remove operation.
   * 
   * @param userId - current user's PIC
   * @param customerPic - child's PIC (not required, can be empty string)
   * @param dataType - data type identifier, for example "pyh.family.community"
   * @param message - log message
   */
  public void remove(String userId, String customerPic, String dataType, String message) {
    log(Log.REMOVE, userId, customerPic, dataType, message);
  }
  
  /**
   * @return - LogServicePortType instance
   */
  private LogServicePortType getLogService() {
    
    LogServiceFactory logServiceFactory = new LogServiceFactory(PyhConstants.LOK_SERVICE_USER_ID, PyhConstants.LOK_SERVICE_PASSWORD, PyhConstants.LOK_SERVICE_ENDPOINT);
    return logServiceFactory.getLogService();
  }
  
  /**
   * Creates an AuditInfo for log web service call.
   * 
   * @param userId - current user's PIC
   * @return - AuditInfoType
   */
  private AuditInfoType getLogAuditInfo(String userId) {
    
    AuditInfoType logAuditInfoType = new fi.koku.services.utility.log.v1.AuditInfoType();
    logAuditInfoType.setComponent(PyhConstants.COMPONENT_PYH);
    logAuditInfoType.setUserId(userId);
    
    return logAuditInfoType;
  }
  
  /**
   * Call the log web service.
   * 
   * @param operation - operation type
   * @param userId - current user's PIC
   * @param customerPic - child's PIC (not required, can be empty string)
   * @param dataType - data type identifier, for example "pyh.family.community"
   * @param message - log message
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
      // PYH operates normally even if logging fails
      logger.error("LOK service failed to log operation " + operation + " for data type " + dataType, fault);
    }
  }
}
