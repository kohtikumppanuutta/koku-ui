package fi.koku.portlet.filter.userinfo;

public class LooraUserInfo extends UserInfo {
  
  public LooraUserInfo() {
    super();
  }

  public LooraUserInfo(String uid, String pic, String fname, String sname) {
    super(uid, pic, fname, sname);
  }

  @Override
  public boolean hasStrongAuthentication() {
    return true;
  }

  @Override
  boolean isStrongAuthenticationEnabled() {
    return false;
  }

  @Override
  void setStrongAuthentication(boolean strongAuthentication) {    
  }

}
