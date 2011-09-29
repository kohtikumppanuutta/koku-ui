package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.koku.pyh.MessageController;

public class Message {

  private static Logger log = LoggerFactory.getLogger(Message.class);
  
  boolean debug = true;
  
  private String id;
  private String from;
  private String text;

  public Message(String id, String fromUserPic, String text) {
    if (debug) {
      log.info("Message.constructor: creating a Message with parameters:");
      log.info("id: " + id);
      log.info("fromUserPic: " + fromUserPic);
      log.info("text: " + text);
    }
    
    this.id = id;
    this.from = fromUserPic;
    this.text = text;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
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
  
}
