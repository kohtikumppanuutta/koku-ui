package fi.koku.lok;

import java.util.Locale;

import fi.koku.services.entity.userinfo.v1.model.Role;

/**
 * Log constants.
 * 
 * @author aspluma
 */
public class LogConstants {
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final Locale LOCALE_FI = new Locale("fi");
  public static final boolean REAL_LOG = true; // use real/mock log or use demo
                                               // factory
  public static final boolean REAL_ADMIN_LOG = true;

  public static final String LOG_NORMAL = "loki";
  public static final String LOG_ADMIN = "seurantaloki";

  public static final String LOG_USERNAME = "marko";
  public static final String LOG_PASSWORD = "marko";
  
  public static final String CUSTOMER_SERVICE_ENDPOINT = "http://localhost:8180/customer-service-0.0.1-SNAPSHOT/CustomerServiceEndpointBean?wsdl";
  public static final String CUSTOMER_SERVICE_USER_ID = "marko";
  public static final String CUSTOMER_SERVICE_PASSWORD = "marko";

  public static final String COMPONENT_LOK = "LOK";
  public static final String LOK_USER_ID = "susanna";

  // String ep =
  // "http://localhost:8080/log-service-0.0.1-SNAPSHOT/LogServiceBean?wsdl";
  // TODO: Muuta tämä, kun siirrytään pois mockista!
 // public static final String LOG_SERVICE = "http://localhost:8088/mocklogService-soap11-binding?WSDL";
//24.9. vaihda portiksi 8580, jos tutkitaan tcpmonilla
//  public static final String LOG_SERVICE_ENDPOINT = "http://localhost:8580/lok-service-0.0.1-SNAPSHOT.jar";
  public static final String LOG_SERVICE_ENDPOINT = "http://localhost:8180/lok-service-0.0.1-SNAPSHOT/LogServiceEndpointBean?wsdl";
  
  public static final String LOG_SERVICE_USER_ID = "marko";
  public static final String LOG_SERVICE_PASSWORD = "marko";
  
  // These are user errors, no error code needed
  public static final String ARCHIVE_INPUT_ERROR = "Arkistointipäivämäärä väärän muotoinen";
  
  public static Role ROLE_LOK_ADMIN = new Role("ROLE_LOK_ADMIN");
  public static Role ROLE_LOK_LOG_ADMIN = new Role("ROLE_LOK_LOG_ADMIN");
  
  // tämä rivi on kopioitu servicesta
  public static final int LOG_NOTHING_TO_ARCHIVE = 2200;
  
  private LogConstants() {
    // Contains only static constants. No need for new instances
  }

}


