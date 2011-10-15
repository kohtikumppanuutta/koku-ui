package fi.koku.lok;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author makinsu
 */
public class AdminLogEntry {
  
  private static final Logger logger = LoggerFactory.getLogger(AdminLogEntry.class);
  
  private String message; // message
 // private String logId; // dataItemId: id given by the logging system
  private Date timestamp; // timestamp
  private String user;  // userPic: pic of the user
  private String customer; // customerPic: pic of the child
 // private String dataItemType;  // dataItemType: kks.vasu, kks.4v, family info, ..
  private String operation;  // operation: create, read, write, ..
//  private String clientSystemId; // clientSystemId: pyh, kks, kunpo, pegasos..


  public Date getTimestamp() {
 //   logger.debug("portlet get timestamp: "+timestamp);
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
   // logger.debug("portlet set timestamp: "+timestamp);
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
 /*   message = logId +" "+ timestamp +" "+ user +" "+ child +" "+ dataItemType +" "+ 
      operation +" "+ clientSystemId;
   */ 
    return message;
  }
 
}
