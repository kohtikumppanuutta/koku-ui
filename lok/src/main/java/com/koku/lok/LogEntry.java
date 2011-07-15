package fi.koku.lok;

/**
 * 
 * @author aspluma
 */
public class LogEntry {
  private String message;

  public LogEntry(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return message;
  }
  
}
