/**
 * 
 */
package fi.koku.portlet.filter.userinfo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.arcusys.tampere.hrsoa.entity.User;
import fi.arcusys.tampere.hrsoa.ws.ldap.LdapService;
import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.service.UserInfoService;
import fi.koku.services.common.kahva.LdapServiceFactory;

/**
 * @author mikkope
 *
 */
public class UserinfoServiceKunpoImpl implements UserInfoService {

  private static final Logger log = LoggerFactory.getLogger(UserinfoServiceKunpoImpl.class);
  
  @Override
  public UserInfo getUserInfoById(String uid) {

    String endpoint = null;
    UserInfo userInfo = new UserInfo();
    try {
      // 1) Call Kahvaservice to get User.ssn
      //Webservice call #TODO# Make endpoint configurable
      endpoint = "http://localhost:8280/kahvaservice-mock-0.0.1-SNAPSHOT/KahvaServiceEndpointBean";
      LdapServiceFactory f = new LdapServiceFactory(endpoint);
      LdapService ws = f.getOrganizationService();
      User userFromWS = ws.getUserById(uid);
      
      //Create UserInfo and store it into portlet session
      userInfo.setUid(uid);
      userInfo.setPic(userFromWS.getSsn());
      userInfo.setFname(userFromWS.getFirstName());
      userInfo.setSname(userFromWS.getLastName());
      userInfo.setEmail(userFromWS.getEmail());
            
    } catch (Exception e) {
       log.error("Failed to get User data from external source: WS.endpoint="+endpoint);
       //log.debug("Detailed info:",e);
    }
    return userInfo;
  }

}
