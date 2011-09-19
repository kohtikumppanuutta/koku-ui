package fi.koku.portlet.filter.userinfo;

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

import fi.koku.portlet.filter.userinfo.service.UserInfoService;

/**
 * @author mikkope
 * 
 */

public class UserInfoPortletFilter implements RenderFilter {

  private static final Logger log = LoggerFactory.getLogger(UserInfoPortletFilter.class);
  private UserInfoService userInfoService = null;
  
  
  public void init(FilterConfig filterConfig) {
    
    //This just an example how to get information from portlet.xml
    String implClassName = filterConfig.getInitParameter("authImplClassName");
    log.debug("Initializing portlet: authImplClassName=" + implClassName);

    //Load configured class
    try {
      userInfoService = (UserInfoService)Class.forName(implClassName).newInstance();
    } catch (InstantiationException e) {
      log.error("Failed to Instantiate classname="+implClassName,e);
    } catch (IllegalAccessException e) {
      log.error("Failed to Instantiate. Illegal Access on classname="+implClassName,e);
    } catch (ClassNotFoundException e) {
      log.error("Class not found. Classname="+implClassName,e);
    }
    
    

  }

  public void doFilter(RenderRequest request, RenderResponse response, FilterChain filterChain) {
    log.debug("Entered UserInfo filter");

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
          UserInfo userInfo = userInfoService.getUserInfoById(portalUserId);
          if(userInfo!=null){
            psession.setAttribute(UserInfo.KEY_USER_INFO, userInfo);
            log.debug("Following UserInfo-object is added to portlet sesssion : "+userInfo);
          }else{
            log.debug("userInfo=null and therefore not added to portletsession");
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