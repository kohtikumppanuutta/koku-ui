/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.lok;

import java.util.Date;

/**
 * Data class for admin log entry.
 * 
 * @author makinsu
 */
public class AdminLogEntry {
  
  private String message; // message
  private Date timestamp; // timestamp
  private String user;  // userPic: pic of the user
  private String customer; // customerPic: pic of the child
  private String operation;  // operation: create, read, write, ..
  

  public Date getTimestamp() {
    return timestamp;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  // format for timestamp: yyyy-mm-dd
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public AdminLogEntry(){  
  }
  
  public AdminLogEntry(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return message;
  }
}