package com.ixonos.koku.lok;

/**
 * 
 * @author aspluma
 */
public class LogEntry {
  private String message;
  private String log_id;
  private String timestamp;
  private String user;
  private String child;
  private String event_type;
  private String event_description;
  private String calling_system;
  

  public String getLog_id() {
    return log_id;
  }

  public void setLog_id(String log_id) {
    this.log_id = log_id;
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

  public String getEvent_type() {
    return event_type;
  }

  public void setEvent_type(String event_type) {
    this.event_type = event_type;
  }

  public String getEvent_description() {
    return event_description;
  }

  public void setEvent_description(String event_description) {
    this.event_description = event_description;
  }

  public String getCalling_system() {
    return calling_system;
  }

  public void setCalling_system(String calling_system) {
    this.calling_system = calling_system;
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
    message = log_id +" "+ timestamp +" "+ user +" "+ child +" "+ event_type +" "+ 
      event_description +" "+ calling_system;
    
    return message;
  }
 
}
