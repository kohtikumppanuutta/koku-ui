package com.ixonos.koku.lok;

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
  private String eventType = "";
  private String eventDescription = "";
  private String callingSystem = "";

  public static final int BASIC_LOG = 0; //Tapahtumaloki
  public static final int MANIPULATION_LOG = 1; //Tapahtumalokin kasittelyloki
  
  private Random generator = new Random();
  private String[] eventTypes = {"luku", "kirjoitus", "poisto", "luonti"};
  private String[] manipulationLogEventTypes = {"arkistointi", "luku"};
  private String[] eventDescriptions = {"4v-suunnitelma", "erityisruokavalio", "vasu", "osoitetiedot", "perhetiedot"};
  private String[] callingSystems = {"KKS", "PYH", "TIVA", "KV"};
 
  /*
   * Create a demo log entry 
   */
  LogEntry createLogEntry(int id, int logType){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh.mm:ss");
    LogEntry entry = new LogEntry();
    
    user = "kayttaja"+id;
    child = "child"+id*2;
    int nr = generator.nextInt(4);
 
    if(MANIPULATION_LOG==logType){
      nr = generator.nextInt(manipulationLogEventTypes.length);
      eventType = manipulationLogEventTypes[nr];
    }else{
      eventType = eventTypes[nr];
    }
    nr = generator.nextInt(5);
    eventDescription = eventDescriptions[nr];
    nr = generator.nextInt(4);
    callingSystem = callingSystems[nr];
    
    entry.setLogId(""+id);
          
    entry.setTimestamp(simpleDateFormat.format(new Date()));
    entry.setUser(user);
    entry.setChild(child);
    entry.setEventType(eventType);
    entry.setEventDescription(eventDescription);
    entry.setCallingSystem(callingSystem);
    
    return entry;
  }
}
