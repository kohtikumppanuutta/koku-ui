package com.ixonos.koku.lok;

/**
 * 
 * @author aspluma
 */
public class LogEntry {
  private String message;
  private String logId;
  private String timestamp;
  private String user;
  private String child;
  private String eventType;
  private String eventDescription;
  private String callingSystem;
  

  public String getLogId() {
    return logId;
  }

  public void setLogId(String logId) {
    this.logId = logId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
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

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public String getEventDescription() {
    return eventDescription;
  }

  public void setEventDescription(String eventDescription) {
    this.eventDescription = eventDescription;
  }

  public String getCallingSystem() {
    return callingSystem;
  }

  public void setCallingSystem(String callingSystem) {
    this.callingSystem = callingSystem;
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
    message = logId +" "+ timestamp +" "+ user +" "+ child +" "+ eventType +" "+ 
      eventDescription +" "+ callingSystem;
    
    return message;
  }
 
}
