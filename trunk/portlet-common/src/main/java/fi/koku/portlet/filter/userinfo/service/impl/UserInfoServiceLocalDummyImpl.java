/**
 * Dummy implementation of userInfoService interface for local testing
 */
package fi.koku.portlet.filter.userinfo.service.impl;

import java.util.HashMap;
import java.util.Map;

import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.UserInfoFactory;
import fi.koku.portlet.filter.userinfo.service.UserInfoService;

/**
 * @author mikkope
 * 
 */
public class UserInfoServiceLocalDummyImpl implements UserInfoService {
  private Map<String, UserInfo> uidMap = new HashMap<String, UserInfo>();
  private Map<String, UserInfo> picMap = new HashMap<String, UserInfo>();

  @Deprecated
  private UserInfoServiceLocalDummyImpl() {

    UserInfoFactory uif = UserInfoFactory.instance();

    UserInfo[] users = new UserInfo[] {
        
        
        //OLD USERS
        uif.createUserInfo("isa", "111111-1111", "Isa", "Iisakkila"),
        uif.createUserInfo("aiti", "222222-2222", "Aiti", "Aitanen"),
        uif.createUserInfo("mummo", "333333-3333", "Mummo", "Mummolainen"),
        uif.createUserInfo("virkailija", "444444-4444", "Ville", "Virkailija"),
        uif.createUserInfo("superadmin", "310333-0303", "Sanna", "Superpaakayttaja"),
        uif.createUserInfo("admin", "170777-777X", "Pekka", "Paakayttaja"),
        //new users
        uif.createUserInfo("kirsi.kuntalainen", "222222-2222", "Kirsi", "Kuntalainen"),
        uif.createUserInfo("kalle.kuntalainen", "111111-1111", "Kalle", "Kuntalainen"),
        uif.createUserInfo("kaisa.kuntalainen", "444444-4444", "Kaisa", "Kuntalainen"),
        uif.createUserInfo("kauko.kuntalainen", "555555-5555", "Kauko", "Kuntalainen"),
        uif.createUserInfo("kerttu.kuntalainen", "333333-3333", "Kerttu", "Kuntalainen"),
        uif.createUserInfo("keijo.keinonen", "131313-1313", "Keijo", "Keinonen"),
        uif.createUserInfo("liisa.lahtinen", "141414-1414", "Liisa", "Lahtinen"),
        uif.createUserInfo("lauri.lahtinen", "181818-1818", "Lauri", "Lahtinen"),
        uif.createUserInfo("lasse.lahtinen", "191919-1919", "Lasse", "Lahtinen"),
        uif.createUserInfo("saara.salminen", "202020-2020", "Saara", "Salminen"),
        uif.createUserInfo("sami.salminen", "151515-1515", "Sami", "Salminen"),
        uif.createUserInfo("saana.salminen", "212121-2121", "Saana", "Salminen"),
        uif.createUserInfo("sanni.suhonen", "161616-1616", "Sanni", "Suhonen"),
        uif.createUserInfo("sampo.suhonen", "232323-2323", "Sampo", "Suhonen"),
        uif.createUserInfo("santtu.suhonen", "242424-2424", "Santtu", "Suhonen"),
        uif.createUserInfo("minna.mieho", "252525-2525", "Minna", "Mieho"),
        uif.createUserInfo("martti.mieho", "262626-2626", "Martti", "Mieho"),
        uif.createUserInfo("malla.mieho", "272727-2727", "Malla", "Mieho"),
        uif.createUserInfo("sulo.simonen", "171717-1717", "Sulo", "Simonen"),
        uif.createUserInfo("paivi.paivakoti", "777777-7777", "Paivi", "Paivakoti"),
        uif.createUserInfo("petra.paivakoti", "282828-2828", "Paivi", "Paivakoti"),
        uif.createUserInfo("nelli.neuvola", "888888-8888", "Nelli", "Neuvola"),
        uif.createUserInfo("niina.neuvola", "292929-2929", "Nelli", "Neuvola"),
        uif.createUserInfo("veeti.virkamies", "999999-9999", "Veeti", "Virkamies"),
        uif.createUserInfo("pertti.paakayttaja", "101010-1010", "Pertti", "Paakayttaja"),
        uif.createUserInfo("piia.paakayttaja", "121212-1212", "Piia", "Paakayttaja"),
    };
    for(int i=0; i<users.length; i++) {
      uidMap.put(users[i].getUid(), users[i]);
      picMap.put(users[i].getPic(), users[i]);
    }
  }
  
  public UserInfo getUserInfoById(String uid) {
    return uidMap.get(uid);
  }
  
  public UserInfo getUserInfoByPic(String pic) {
    return picMap.get(pic);
  }
}
