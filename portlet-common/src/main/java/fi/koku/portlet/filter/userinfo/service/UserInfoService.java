/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
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
