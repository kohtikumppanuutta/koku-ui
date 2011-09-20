package fi.koku.demo;

import fi.koku.lok.KoKuWSFactory;

import fi.koku.services.utility.log.v1.AuditInfoType;
import fi.koku.services.utility.log.v1.LogEntriesType;
import fi.koku.services.utility.log.v1.LogEntryType;
import fi.koku.services.utility.log.v1.LogQueryCriteriaType;
import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.services.utility.log.v1.ServiceFault;


public class LogClient {
  
  public static void main(String ... args) throws ServiceFault {
    KoKuWSFactory f = new KoKuWSFactory();
    LogServicePortType s = f.getLogService();

    LogEntriesType r = s.opQueryLog(new LogQueryCriteriaType(), new AuditInfoType());
    for(LogEntryType e : r.getLogEntry()) {
      System.out.println("e: "+e);
    }
  }

}
