package com.ixonos.koku.pyh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.services.utility.log.v1.LogServicePortType;
import fi.koku.settings.KoKuPropertiesUtil;

public class Log {

  private Log() { 
  }
  
  private static final Logger logger = LoggerFactory.getLogger(Log.class);
  
  public static final String CREATE = "create";
  public static final String ENDPOINT = KoKuPropertiesUtil.get("lok.service.endpointaddress");
  
  public static void create(String customerPic, String userId, String dataType, String message) {
    log(Log.CREATE, customerPic, userId, dataType, message);
  }
  
  private static LogServicePortType getLogService() {
    
    // TODO
    
    return null;
  }
  
  private static void log(String operation, String customerPic, String userId, String dataType, String message) {
    
    // TODO
    
    // LogServicePortType log = getLogService();
    
  }
  
}
