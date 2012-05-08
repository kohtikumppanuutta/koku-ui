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
/**
 * 
 */
package fi.koku.lok.model;

/**
 * Data class for user data. Created from Customer service data.
 * 
 * @author mikkope
 */
public class User {

  private String pic;
  private String uid;
  private String fname;
  private String sname;
 
  public User(String pic, String uid, String fname, String sname) {
    super();
    this.pic = pic;
    this.uid = uid;
    this.fname = fname;
    this.sname = sname;
  }

  public String getPic() {
    return pic;
  }

  public void setPic(String pic) {
    this.pic = pic;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getFname() {
    return fname;
  }

  public void setFname(String fname) {
    this.fname = fname;
  }

  public String getSname() {
    return sname;
  }

  public void setSname(String sname) {
    this.sname = sname;
  }

  @Override
  public String toString() {
    return "User [pic=" + pic + ", uid=" + uid + ", fname=" + fname + ", sname=" + sname + "]";
  }
}