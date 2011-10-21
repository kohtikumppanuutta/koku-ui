/**
 * This class holds information about the user. The actual data is retrieved from external sources
 * in portlet filter
 */
package fi.koku.portlet.filter.userinfo;

/**
 * @author mikkope
 *
 */
public abstract class UserInfo {
  
  public static final String KEY_USER_INFO = "fi.koku.userinfo";
  public static final String KEY_VETUMA_AUTHENTICATION_URL = "fi.koku.vetuma_authentication_url";
  
  private String pic;
  private String uid;
  private String fname;
  private String sname;
  private String email;
  
  public UserInfo() {  
  }
  
  public UserInfo(String uid, String pic, String fname, String sname) {
    this.uid = uid;
    this.pic = pic;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "UserInfo [pic=" + pic + ", uid=" + uid + ", fname=" + fname + ", sname=" + sname + ", email=" + email + "]";
  }

  public abstract boolean isStrongAuthenticationEnabled();
  abstract void setStrongAuthentication(boolean strongAuthentication);
  public abstract boolean hasStrongAuthentication();

}
