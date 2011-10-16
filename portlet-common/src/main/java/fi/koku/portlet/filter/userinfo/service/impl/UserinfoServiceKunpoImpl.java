/**
 * This implementation gets user information from Kuntalaisportaali environment
 */
package fi.koku.portlet.filter.userinfo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.koku.portlet.filter.userinfo.UserInfo;
import fi.koku.portlet.filter.userinfo.UserInfoConstants;
import fi.koku.portlet.filter.userinfo.service.UserInfoService;
import fi.koku.services.entity.person.v1.Person;
import fi.koku.services.entity.person.v1.PersonConstants;
import fi.koku.services.entity.person.v1.PersonService;

/**
 * @author mikkope
 *
 */
public class UserinfoServiceKunpoImpl implements UserInfoService {

  private static final Logger LOG = LoggerFactory.getLogger(UserinfoServiceKunpoImpl.class);
  
  @Override
    public UserInfo getUserInfoById(String uid) {
      
      UserInfo ret = null;
      
      if(StringUtils.isNotBlank(uid)){
          PersonService ps = new PersonService();
          List<String> user = new ArrayList<String>(1);
          user.add(uid);//Add current user
          
          try{
            List<Person> persons = ps.getPersonsByUids(user, PersonConstants.PERSON_SERVICE_DOMAIN_CUSTOMER, uid, UserInfoConstants.COMPONENT_USER_INFO_FILTER);
            Person p = persons.get(0);//Get the first one (actually should there should be only 0 or 1 persons in list)
            ret = new UserInfo(uid, p.getPic(), p.getFname(), p.getSname());
          }catch(Exception e){
            LOG.error("Person not found with uid="+uid);
          }
      }
      return ret;
  }

}
