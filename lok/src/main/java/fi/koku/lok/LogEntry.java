package fi.koku.lok;

/**
 * 
 * @author aspluma
 */
public class LogEntry {
  private String message; // message
  private String logId; // dataItemId
  private String timestamp; // timestamp
  private String user;  // userPic
  private String child; // customerPic
  private String eventType;  // dataItemType
  private String eventDescription;  // operation
  private String callingSystem; // clientSystemId
  

  public String getLogId() {
    return logId;
  }

  public void setLogId(String logId) {
    this.logId = logId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  // format for timestamp: yyyy-mm-dd
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
