/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
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

