package com.ixonos.koku.lok;

import java.util.Date;

public class LogArchiveDate {

  private Date date;

  public LogArchiveDate() {
  }
  
  public LogArchiveDate(Date archivedate){
    this.date = archivedate;
  }
  
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}

