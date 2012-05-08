/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.lok;

import java.util.Date;

/**
 * Data class for log archive date data.
 */
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

