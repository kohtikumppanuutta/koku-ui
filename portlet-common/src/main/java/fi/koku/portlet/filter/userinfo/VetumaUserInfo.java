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
  boolean isStrongAuthenticationEnabled() {
    return true;
  }

  @Override
  void setStrongAuthentication(boolean strongAuthentication) {
    this.strongAuthentication = strongAuthentication;
  }

}
