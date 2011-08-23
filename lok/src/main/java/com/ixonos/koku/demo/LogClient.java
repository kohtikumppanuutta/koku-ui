package com.ixonos.koku.demo;

import com.ixonos.koku.lok.KoKuWSFactory;

import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogServicePortType;


public class LogClient {
  
  public static void main(String ... args) {
    KoKuWSFactory f = new KoKuWSFactory();
    LogServicePortType s = f.getLogService();

    LogEntriesType r = s.opQueryLog(new LogQueryCriteriaType());
    for(LogEntryType e : r.getLogEntry()) {
      System.out.println("e: "+e);
    }
  }

}
