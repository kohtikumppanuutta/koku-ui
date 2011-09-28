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

   if ("kirsi.kuntalainen".equals(uid)) {
    u.setUid("kirsi.kuntalainen");
    u.setPic("222222-2222");
    u.setFname("Kirsi");
    u.setSname("Kuntalainen");
  } else if ("kalle.kuntalainen".equals(uid)) {
    u.setUid("kalle.kuntalainen");
    u.setPic("111111-1111");
    u.setFname("Kalle");
    u.setSname("Kuntalainen");
  } else if ("kaisa.kuntalainen".equals(uid)) {
    u.setUid("kaisa.kuntalainen");
    u.setPic("444444-4444");
    u.setFname("Kaisa");
    u.setSname("Kuntalainen");
  } else if ("kauko.kuntalainen".equals(uid)) {
    u.setUid("kauko.kuntalainen");
    u.setPic("555555-5555");
    u.setFname("Kauko");
    u.setSname("Kuntalainen");
  } else if ("keijo.keinonen".equals(uid)) {
    u.setUid("keijo.keinonen");
    u.setPic("131313-1313");
    u.setFname("Keijo");
    u.setSname("Kepuli");
  } else if ("kerttu.kuntalainen".equals(uid)) {
    u.setUid("kerttu.kuntalainen");
    u.setPic("333333-3333");
    u.setFname("Kerttu");
    u.setSname("Kuntalainen");
  } else if ("paivi.paivakoti".equals(uid)) {
    u.setUid("paivi.paivakoti");
    u.setPic("777777-7777");
    u.setFname("Paivi");
    u.setSname("Paivakoti");
  } else if ("nelli.neuvola".equals(uid)) {
    u.setUid("nelli.neuvola");
    u.setPic("888888-8888");
    u.setFname("Nelli");
    u.setSname("Neuvola");
  } else if ("veeti.virkamies".equals(uid)) {
    u.setUid("veeti.virkamies");
    u.setPic("999999-9999");
    u.setFname("Veeti");
    u.setSname("Virkamies");
  } else if ("pertti.paakayttaja".equals(uid)) {
    u.setUid("pertti.paakayttaja");
    u.setPic("101010-1010");
    u.setFname("Pertti");
    u.setSname("Paakayttaja");
  } else if ("piia.paakayttaja".equals(uid)) {
    u.setUid("piia.paakayttaja");
    u.setPic("121212-1212");
    u.setFname("Piia");
    u.setSname("Paakayttaja");
  
  //OLD USERS  
  } else if ("isa".equals(uid)) {
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
