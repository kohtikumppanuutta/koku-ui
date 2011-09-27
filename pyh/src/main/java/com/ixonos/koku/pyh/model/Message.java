package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.List;

public class Message {

  //private static int codeGenerator = 1;

  private String id;
//  private List<MessageListener> listeners;
//  private List<String> recipients;
  private String from;
  private String text;
//  private String description;
//  private Executable executable;
//  private int executeCount = 0;
//  private int acceptCount = 0;
//  private List<String> read;

//  private Message(String id) {
//    this.id = id;
//    listeners = new ArrayList<MessageListener>();
//    read = new ArrayList<String>();
//    recipients = new ArrayList<String>();
//  }

   private Message() {
     
   }
  
//  public void addListener(MessageListener l) {
//    listeners.add(l);
//  }

//  public void removeListener(MessageListener l) {
//    listeners.remove(l);
//  }

//  public void accept(String id) {
//    acceptCount++;
//    read.add(id);
//
//    // IF ALL RECIPIENTS SHOULD ACCEPT THE MESSAGE BEFORE
//    // ACTION, REMOVE COMMENTS
//    // if (acceptCount == executeCount && executable != null) {
//    executable.execute();
//    notifyRead();
//
//  }

//  public void reject() {
//    notifyRead();
//  }

//  public boolean isReadBy(String id) {
//    return read.contains(id);
//  }

//  private void notifyRead() {
//    List<MessageListener> tmp = new ArrayList<MessageListener>(listeners);
//
//    for (MessageListener m : tmp) {
//      m.remove(this);
//    }
//  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

//  public void addRecipient(String ssn) {
//    recipients.add(ssn);
//    executeCount++;
//  }

//  public List<String> getRecipients() {
//    return recipients;
//  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

//  public String getDescription() {
//    return description;
//  }

//  public void setDescription(String description) {
//    this.description = description;
//  }

//  public Executable getExecutable() {
//    return executable;
//  }
//
//  public void setExecutable(Executable executable) {
//    this.executable = executable;
//  }

//  public static Message createMessage(List<String> recipients, String from, String addedPersonSSN, String message, String desc, Executable e) {
  public static Message createMessage(String id, String fromUserPic, String text) {
//    Message m = new Message("" + codeGenerator++);
//    for (String ssn : recipients) {
//      m.addRecipient(ssn);
//    }
    
    Message message = new Message();
    message.setId(id);
    message.setFrom(fromUserPic);
    message.setText(text);
//    message.setDescription(description);
//    m.setExecutable(e);
    return message;
  }
}
