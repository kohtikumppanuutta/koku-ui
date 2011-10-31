/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
/**
 * 
 */
package fi.koku.portlet.filter.userinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.UserInfoConstants;
import fi.koku.portlet.filter.userinfo.UserInfoFactory;
import fi.koku.portlet.filter.userinfo.service.UserInfoService;
import fi.koku.services.entity.person.v1.Person;
import fi.koku.services.entity.person.v1.PersonConstants;
import fi.koku.services.entity.person.v1.PersonService;

/**
 * This implementation gets user information Loora environment
 * 
 * @author mikkope
 *
 */
public class UserInfoServiceLooraImpl implements UserInfoService{

  private static final Logger LOG = LoggerFactory.getLogger(UserInfoServiceLooraImpl.class);
  
  public UserInfo getUserInfoById(String uid) {
    
    UserInfo ret = null;
    
    if(StringUtils.isNotBlank(uid)){
        PersonService ps = new PersonService();
        List<String> user = new ArrayList<String>(1);
        user.add(uid);//Add current user
        
        try{
          List<Person> persons = ps.getPersonsByUids(user, PersonConstants.PERSON_SERVICE_DOMAIN_OFFICER, uid, UserInfoConstants.COMPONENT_USER_INFO_FILTER);
          Person p = persons.get(0);//Get the first one (actually should there should be only 0 or 1 persons in list)
          ret = UserInfoFactory.instance().createUserInfo(uid, p.getPic(), p.getFname(), p.getSname());
        }catch(Exception e){
          LOG.error("Person not found with with uid="+uid);
        }
    }
    return ret;
  }
}
