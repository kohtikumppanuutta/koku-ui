/**
 * 
 */
package com.ixonos.koku.lok.model;

/**
 * @author mikkope
 *
 */
public class User {

  
  private String ssn;
  private String uid;
  private String fname;
  private String sname;
 
  
  public User(String ssn, String uid, String fname, String sname) {
    super();
    this.ssn = ssn;
    this.uid = uid;
    this.fname = fname;
    this.sname = sname;
  }


  public String getSsn() {
    return ssn;
  }


  public void setSsn(String ssn) {
    this.ssn = ssn;
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
    return "User [ssn=" + ssn + ", uid=" + uid + ", fname=" + fname + ", sname=" + sname + "]";
  }
 
  
}
