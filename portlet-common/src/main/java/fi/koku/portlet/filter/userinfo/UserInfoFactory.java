/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.portlet.filter.userinfo;

import fi.koku.settings.KoKuPropertiesUtil;

public abstract class UserInfoFactory {
  
  private static UserInfoFactory instance;
    
  public static UserInfoFactory instance() {

    if (instance == null) {
    
      String vetumaEnabled = KoKuPropertiesUtil.get("vetuma.enabled");
      
      if (vetumaEnabled != null && "true".equals(vetumaEnabled)) {
        instance = new KunpoUserInfoFactory();
      } else {
        instance = new LooraUserInfoFactory();
      }
      
    }
    
    return instance;    
  }
  
  public abstract UserInfo createUserInfo();
  public abstract UserInfo createUserInfo(String uid, String pic, String fname, String sname);
  
  private static class KunpoUserInfoFactory extends UserInfoFactory {

    @Override
    public UserInfo createUserInfo() {
      return new VetumaUserInfo();
    }

    @Override
    public UserInfo createUserInfo(String uid, String pic, String fname, String sname) {
      return new VetumaUserInfo(uid, pic, fname, sname);
    }
    
  }
  
  private static class LooraUserInfoFactory extends UserInfoFactory {

    @Override
    public UserInfo createUserInfo() {
      return new LooraUserInfo();
    }

    @Override
    public UserInfo createUserInfo(String uid, String pic, String fname, String sname) {
      return new LooraUserInfo(uid, pic, fname, sname);
    }
    
  }

}
