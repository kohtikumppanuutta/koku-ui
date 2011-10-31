/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package com.ixonos.koku.pyh.mock;

public class User {

  private String role;
  private String ssn;

  public User(String ssn, String role) {
    this.ssn = ssn;
    this.role = role;
  }

  public String getSsn() {
    return ssn;
  }

  public String getRole() {
    return role;
  }

}
