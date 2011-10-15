package fi.koku.lok;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author aspluma
 */
public class LogEntry {
  
  private static final Logger logger = LoggerFactory.getLogger(LogEntry.class);
  
  private String message; // message
  private String logId; // dataItemId: id given by the logging system
  private Date timestamp; // timestamp
  private String user;  // userPic: pic of the user
  private String child; // customerPic: pic of the child
  private String dataItemType;  // dataItemType: kks.vasu, kks.4v, family info, ..
  private String operation;  // operation: create, read, write, ..
  private String clientSystemId; // clientSystemId: pyh, kks, kunpo, pegasos..

  public String getLogId() {
    return logId;
  }

  public void setLogId(String logId) {
    this.logId = logId;
  }

  public Date getTimestamp() {
  //  logger.debug("portlet get timestamp: "+timestamp);
    return timestamp;
  }

  // format for timestamp: yyyy-mm-dd
  public void setTimestamp(Date timestamp) {
  //  logger.debug("portlet set timestamp: "+timestamp);
    this.timestamp = timestamp;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getChild() {
    return child;
  }

  public void setChild(String child) {
    this.child = child;
  }

  public String getDataItemType() {
    return dataItemType;
  }

  public void setDataItemType(String dataItemType) {
    this.dataItemType = dataItemType;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getClientSystemId() {
    return clientSystemId;
  }

  public void setClientSystemId(String clientSystemId) {
    this.clientSystemId = clientSystemId;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LogEntry(){  
  }
  
  public LogEntry(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    
    return message;
  }
 
}
