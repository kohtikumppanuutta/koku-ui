package fi.koku.portlet.filter;

/**
 * Portlet filter that uses portal remoteUserId to retrieve actual user from External Webservice.
 * Local UserInfo object is created using external userdata and after that its stored in portlet session
 * and can be accessed with KEY_USER_INFO -key
 */

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.arcusys.tampere.hrsoa.entity.User;
import fi.arcusys.tampere.hrsoa.ws.ldap.LdapService;
import fi.koku.services.common.kahva.LdapServiceFactory;

import fi.koku.portlet.filter.UserInfo;

/**
 * @author mikkope
 * 
 */

public class UserInfoPortletFilter implements RenderFilter {

  private static final Logger log = LoggerFactory.getLogger(UserInfoPortletFilter.class);
  
  public void init(FilterConfig filterConfig) {
    
    //This just an example how to get information from portlet.xml
    String implClassName = filterConfig.getInitParameter("authImplClassName");
    log.debug("Initializing portlet: authImplClassName=" + implClassName);

  }

  public void doFilter(RenderRequest request, RenderResponse response, FilterChain filterChain) {
    log.debug("Entered PYH userinfo filter");

    //Allow filter chaining
    doFilterChaining(request, response, filterChain);
    
    // Get portal user id
    String portalUserId = request.getRemoteUser();
    log.debug("req.getRemoteUser=" + portalUserId);

    // Check if user is authenticated
    if (portalUserId != null) {
      PortletSession psession = request.getPortletSession();

      // Check if portlet-session exists already
      if (psession != null) {
        Object o = psession.getAttribute(UserInfo.KEY_USER_INFO);
        
        // Check if UserInfo
        if (o != null && o instanceof UserInfo) {
          // Portal user has UserInfo object already in portletsession = OK
          log.debug("psession.userInfo="+(UserInfo)o);//#TODO# Consider removing hetu from log
        } else {

          // UserInfo does not exist yet
          
          // #TODO# Here we have to remember that Loora and Kunpo has different
          // mechanisms after VETUMAIntegration. Now impl "Loora-case"

          // 1) Call Kahvaservice to get User.ssn
          String endpoint = null;
          try {
            //Webservice call #TODO# Make endpoint configurable
            endpoint = "http://localhost:8280/kahvaservice-mock-0.0.1-SNAPSHOT/KahvaServiceEndpointBean";
            LdapServiceFactory f = new LdapServiceFactory(endpoint);
            LdapService ws = f.getOrganizationService();
            User userFromWS = ws.getUserById(portalUserId);
            
            //Create UserInfo and store it into portlet session
            UserInfo userInfo = new UserInfo();
            userInfo.setUid(portalUserId);
            userInfo.setPic(userFromWS.getSsn());
            userInfo.setFname(userFromWS.getFirstName());
            userInfo.setSname(userFromWS.getLastName());
            userInfo.setEmail(userFromWS.getEmail());
            
            psession.setAttribute(UserInfo.KEY_USER_INFO, userInfo);
            log.debug("Following UserInfo-object is added to portlet sesssion : "+userInfo);
            
            // *3) Call CustomerService to get Koku-User if needed
            
          } catch (Exception e) {
             log.error("Failed to get User data from external source: WS.endpoint="+endpoint);
             //log.debug("Detailed info:",e);
             
             //#TODO# NOTE: Portlet filter is now only adding user info to portletsession if the data is
             //available. Otherwise nothing is done.
             
          }
          
        }
      } else {
        log.debug("PortletSession=null : user probably has not logged in and this is probably the first request.");
      }

    } else {
      //Not authenticated
      log.debug("Portal remoteUser not found so User has probably not logged in!");
    }

  }

  private void doFilterChaining(RenderRequest request, RenderResponse response, FilterChain filterChain) {
    try {
      filterChain.doFilter(request, response);
    } catch (IOException e) {
      log.error("Failed to doFilterChain", e);
    } catch (PortletException e) {
      log.error("Failed to doFilterChain", e);
    }
    log.debug("Finished filterChaining");
  }

  @Override
  public void destroy() {

  }

}