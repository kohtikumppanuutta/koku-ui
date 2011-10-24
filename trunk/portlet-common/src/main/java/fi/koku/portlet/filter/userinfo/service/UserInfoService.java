/**
 * Class for handling UserInfo related operations
 */
package fi.koku.portlet.filter.userinfo.service;

import fi.koku.portlet.filter.userinfo.UserInfo;

/**
 * @author mikkope
 *
 */
public interface UserInfoService {

  
  /**
   * Gets UserInfo object with uid 
   * 
   * @param uid (example. portalRemoteUserId)
   * @return UserInfo object if found, null if not found (or failure)
   */
  UserInfo getUserInfoById(String uid);  
  
}
