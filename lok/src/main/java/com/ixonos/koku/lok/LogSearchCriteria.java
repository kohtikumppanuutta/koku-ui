package com.ixonos.koku.lok;

import java.util.Date;

/**
 * 
 * @author aspluma
 */
public class LogSearchCriteria {
  private String pic;
  private String concept;
  private Date from;
  private Date to;

  public LogSearchCriteria() {
  }
  
  public LogSearchCriteria(String pic, String concept, Date from, Date to) {
    super();
    this.pic = pic;
    this.concept = concept;
    this.from = from;
    this.to = to;
  }

  public String getPic() {
    return pic;
  }
  public void setPic(String pic) {
    this.pic = pic;
  }
  public String getConcept() {
    return concept;
  }
  public void setConcept(String concept) {
    this.concept = concept;
  }
  public Date getFrom() {
    return from;
  }
  public void setFrom(Date from) {
    this.from = from;
  }
  public Date getTo() {
    return to;
  }
  public void setTo(Date to) {
    this.to = to;
  }
  
}
