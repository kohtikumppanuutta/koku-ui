/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
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

}
