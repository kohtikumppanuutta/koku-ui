/**
 * Dummy implementation of userInfoService interface for local testing
 */
package fi.koku.portlet.filter.userinfo.service.impl;

import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.service.UserInfoService;

/**
 * @author mikkope
 * 
 */
public class UserInfoServiceLocalDummyImpl implements UserInfoService {

  public UserInfo getUserInfoById(String uid) {
    UserInfo u = new UserInfo();

    if ("isa".equals(uid)) {
      u.setUid("isa");
      u.setPic("111111-1111");
      u.setFname("Isa");
      u.setSname("Iisakkila");
    } else if ("aiti".equals(uid)) {
      u.setUid("aiti");
      u.setPic("222222-2222");
      u.setFname("Aiti");
      u.setSname("Aitanen");
    } else if ("mummo".equals(uid)) {
      u.setUid("mummo");
      u.setPic("333333-3333");
      u.setFname("Mummo");
      u.setSname("Mummolainen");
    } else if ("virkailija".equals(uid)) {
      u.setUid("virkailija");
      u.setPic("444444-4444");
      u.setFname("Ville");
      u.setSname("Virkailija");
    } else if ("superadmin".equals(uid)){
      u.setUid("superadmin");
      u.setPic("310333-0303");
      u.setFname("Sanna");
      u.setSname("Superpaakayttaja");
    } else if ("admin".equals(uid)){
      u.setUid("admin");
      u.setPic("170777-777X");
      u.setFname("Pekka");
      u.setSname("Paakayttaja");
    }else{
      u = null;
    }
    return u;
  }
}
