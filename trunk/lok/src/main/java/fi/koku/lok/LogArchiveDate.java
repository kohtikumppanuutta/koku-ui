package fi.koku.lok;

import java.util.Date;

public class LogArchiveDate {

  private Date endDate;

  public LogArchiveDate() {
  }
  
  public LogArchiveDate(Date archivedate){
    this.endDate = archivedate;
  }
  
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date date) {
    this.endDate = date;
  }
}

