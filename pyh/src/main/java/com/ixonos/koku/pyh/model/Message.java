package com.ixonos.koku.pyh.model;

import java.util.ArrayList;
import java.util.List;

public class Message {

  private String id;
  private String from;
  private String text;

  public Message(String id, String fromUserPic, String text) {
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
