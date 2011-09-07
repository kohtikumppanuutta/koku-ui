package fi.koku.lok;

import java.util.Locale;

/**
 * Log constants.
 * 
 * @author aspluma
 */
public final class LogConstants {
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final Locale LOCALE_FI = new Locale("fi");
  public static final boolean REAL_LOG = true; // use real/mock log or use demo
                                               // factory
  public static final boolean REAL_ADMIN_LOG = true;

  public static final String LOG_NORMAL = "loki";
  public static final String LOG_ADMIN = "seurantaloki";

  public static final String LOG_USERNAME = "marko";
  public static final String LOG_PASSWORD = "marko";

  // String ep =
  // "http://localhost:8080/log-service-0.0.1-SNAPSHOT/LogServiceBean?wsdl";
  // TODO: Muuta tämä, kun siirrytään pois mockista!
  public static final String LOG_SERVICE = "http://localhost:8088/mocklogService-soap11-binding?WSDL";

  private LogConstants() {
    // Contains only static constants. No need for new instances
  }

}
