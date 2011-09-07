package fi.koku.lok;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This class creates log entries for demo purposes
 * 
 * @author makinsu
 * 
 */
public class LogDemoFactory {

  private String user = "";
  private String child = "";
  private String dataItemType = "";
  private String operation = "";
  private String clientSystemId = "";

  public static final int BASIC_LOG = 0; // Tapahtumaloki
  public static final int MANIPULATION_LOG = 1; // Tapahtumalokin kasittelyloki

  private Random generator = new Random();
  private String[] operations = { "luku", "kirjoitus", "poisto", "luonti" };
  private String[] manipulationLogOperations = { "arkistointi", "luku" };
  private String[] dataItemTypes = { "4v-suunnitelma", "erityisruokavalio", "vasu", "osoitetiedot", "perhetiedot" };
  private String[] callingSystems = { "KKS", "PYH", "TIVA", "KV" };

  /*
   * Create a demo log entry
   */
  LogEntry createLogEntry(int id, int logType) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh.mm:ss");
    LogEntry entry = new LogEntry();

    user = "kayttaja" + id;
    child = "child" + id * 2;
    int nr = 0;

    if (MANIPULATION_LOG == logType) {
      nr = generator.nextInt(manipulationLogOperations.length);
      operation = manipulationLogOperations[nr];
    } else {
      nr = generator.nextInt(operations.length);
      operation = operations[nr];
    }
    
    nr = generator.nextInt(dataItemTypes.length);
    dataItemType = dataItemTypes[nr];
    
    nr = generator.nextInt(callingSystems.length);
    clientSystemId = callingSystems[nr];

    entry.setLogId("" + id);

    entry.setTimestamp(simpleDateFormat.format(new Date()));
    entry.setUser(user);
    entry.setChild(child);
    entry.setDataItemType(dataItemType);
    entry.setOperation(operation);
    entry.setClientSystemId(clientSystemId);

    return entry;
  }
}
