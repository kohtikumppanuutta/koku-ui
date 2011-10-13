/**
 * Dummy implementation of userInfoService interface for local testing
 */
package fi.koku.portlet.filter.userinfo.service.impl;

import java.util.HashMap;
import java.util.Map;

import fi.koku.portlet.filter.userinfo.UserInfo;
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
    UserInfo[] users = new UserInfo[] {
        //OLD USERS
        new UserInfo("isa", "111111-1111", "Isa", "Iisakkila"),
        new UserInfo("aiti", "222222-2222", "Aiti", "Aitanen"),
        new UserInfo("mummo", "333333-3333", "Mummo", "Mummolainen"),
        new UserInfo("virkailija", "444444-4444", "Ville", "Virkailija"),
        new UserInfo("superadmin", "310333-0303", "Sanna", "Superpaakayttaja"),
        new UserInfo("admin", "170777-777X", "Pekka", "Paakayttaja"),
        //new users
        new UserInfo("kirsi.kuntalainen", "222222-2222", "Kirsi", "Kuntalainen"),
        new UserInfo("kalle.kuntalainen", "111111-1111", "Kalle", "Kuntalainen"),
        new UserInfo("kaisa.kuntalainen", "444444-4444", "Kaisa", "Kuntalainen"),
        new UserInfo("kauko.kuntalainen", "555555-5555", "Kauko", "Kuntalainen"),
        new UserInfo("kerttu.kuntalainen", "333333-3333", "Kerttu", "Kuntalainen"),
        new UserInfo("keijo.keinonen", "131313-1313", "Keijo", "Keinonen"),
        new UserInfo("liisa.lahtinen", "141414-1414", "Liisa", "Lahtinen"),
        new UserInfo("lauri.lahtinen", "181818-1818", "Lauri", "Lahtinen"),
        new UserInfo("lasse.lahtinen", "191919-1919", "Lasse", "Lahtinen"),
        new UserInfo("saara.salminen", "202020-2020", "Saara", "Salminen"),
        new UserInfo("sami.salminen", "151515-1515", "Sami", "Salminen"),
        new UserInfo("saana.salminen", "212121-2121", "Saana", "Salminen"),
        new UserInfo("sanni.suhonen", "161616-1616", "Sanni", "Suhonen"),
        new UserInfo("sampo.suhonen", "232323-2323", "Sampo", "Suhonen"),
        new UserInfo("santtu.suhonen", "242424-2424", "Santtu", "Suhonen"),
        new UserInfo("minna.mieho", "252525-2525", "Minna", "Mieho"),
        new UserInfo("martti.mieho", "262626-2626", "Martti", "Mieho"),
        new UserInfo("malla.mieho", "272727-2727", "Malla", "Mieho"),
        new UserInfo("sulo.simonen", "171717-1717", "Sulo", "Simonen"),
        new UserInfo("paivi.paivakoti", "777777-7777", "Paivi", "Paivakoti"),
        new UserInfo("petra.paivakoti", "282828-2828", "Paivi", "Paivakoti"),
        new UserInfo("nelli.neuvola", "888888-8888", "Nelli", "Neuvola"),
        new UserInfo("niina.neuvola", "292929-2929", "Nelli", "Neuvola"),
        new UserInfo("veeti.virkamies", "999999-9999", "Veeti", "Virkamies"),
        new UserInfo("pertti.paakayttaja", "101010-1010", "Pertti", "Paakayttaja"),
        new UserInfo("piia.paakayttaja", "121212-1212", "Piia", "Paakayttaja"),
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
