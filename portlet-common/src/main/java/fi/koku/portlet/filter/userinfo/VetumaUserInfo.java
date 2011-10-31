/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.portlet.filter.userinfo;

public class VetumaUserInfo extends UserInfo {

  private boolean strongAuthentication;
    
  public VetumaUserInfo() {
    super();
  }

  public VetumaUserInfo(String uid, String pic, String fname, String sname) {
    super(uid, pic, fname, sname);
  }

  @Override
  public boolean hasStrongAuthentication() {
    return strongAuthentication;
  }

  @Override
  public boolean isStrongAuthenticationEnabled() {
    return true;
  }

  @Override
  void setStrongAuthentication(boolean strongAuthentication) {
    this.strongAuthentication = strongAuthentication;
  }

}
