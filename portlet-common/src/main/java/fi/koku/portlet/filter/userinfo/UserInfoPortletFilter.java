/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.portlet.filter.userinfo;

/**
 * Portlet filter that uses portal remoteUserId to retrieve actual user from External Webservice.
 * Local UserInfo object is created using external userdata and after that its stored in portlet session
 * and can be accessed with KEY_USER_INFO -key
 */

import java.io.IOException;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.EventFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;
import javax.portlet.filter.ResourceFilter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ixonos.authentication.AuthenticationServiceClient;
import com.ixonos.authentication.AuthenticationServiceClientImpl;
import com.ixonos.authentication.VetumaUserInfo;

import fi.koku.portlet.filter.userinfo.service.UserInfoService;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * @author mikkope
 * 
 */

public class UserInfoPortletFilter implements RenderFilter, ActionFilter, EventFilter, ResourceFilter {

  private static final Logger log = LoggerFactory.getLogger(UserInfoPortletFilter.class);
  private UserInfoService userInfoService = null;
  private FilterConfig filterConfig;

  private AuthenticationServiceClient authServiceClient = null;
  private boolean VETUMA_PROVIDES_INFO = Boolean.valueOf(KoKuPropertiesUtil.get("koku.vetuma.provides.info"));

  /**
   * Initialize portlet filter.
   */
  public void init(FilterConfig filterConfig) {

    log.debug("Initializing portlet: authImplClassName=" + UserInfoConstants.AUTH_IMPL_CLASS_NAME);
    this.filterConfig = filterConfig;
    // Load configured class
    try {

      userInfoService = (UserInfoService) this.getClass().getClassLoader()
          .loadClass(UserInfoConstants.AUTH_IMPL_CLASS_NAME).newInstance();

      String context = KoKuPropertiesUtil.get("environment.name");

      if ("kunpo".equals(context) || VETUMA_PROVIDES_INFO  ) {
        authServiceClient = AuthenticationServiceClientImpl.instance();
      } else {
        authServiceClient = new AuthenticationServiceClient() {
          // Dummy implementation for Loora. Should never be used.
          public boolean hasStrongAuthentication(PortletRequest request) {
            return false;
          }

          public VetumaUserInfo getVetumaUserInfo(PortletRequest request) {
            return null;
          }

          public String getAuthenticationURL(RenderResponse response, String id) {
            return null;
          }

          public boolean hasStrongAuthentication(PortletRequest request, String pic) {
            return false;
          }
        };

      }

    } catch (InstantiationException e) {
      log.error("Failed to Instantiate classname=" + UserInfoConstants.AUTH_IMPL_CLASS_NAME, e);
    } catch (IllegalAccessException e) {
      log.error("Failed to Instantiate. Illegal Access on classname=" + UserInfoConstants.AUTH_IMPL_CLASS_NAME, e);
    } catch (ClassNotFoundException e) {
      log.error("Class not found. Classname=" + UserInfoConstants.AUTH_IMPL_CLASS_NAME, e);
    }

  }

  /**
   * RenderPhase pre-filter : Get userData with remoteId and add UserInfo-object
   * to portlet session
   */
  public void doFilter(RenderRequest request, RenderResponse response, FilterChain filterChain) throws IOException,
  PortletException {
    log.debug("Entered UserInfo filter on renderPhase");

    addUserInfoToSession(request, response);

    if (isAuthorized(request, response)) {
      // Allow filter chaining
      try {
        filterChain.doFilter(request, response);
      } catch (IOException e) {
        log.error("Failed to doFilterChain", e);
      } catch (PortletException e) {
        log.error("Failed to doFilterChain", e);
      }
    }
    log.debug("Finished renderPhase filterChaining");

  }

  /**
   * ActionPhase pre-filter : Get userData with remoteId and add UserInfo-object
   * to portlet session
   */
  public void doFilter(ActionRequest actReq, ActionResponse actRes, FilterChain filterChain) throws IOException,
      PortletException {
    log.debug("Entered UserInfo filter on actionPhase");

    addUserInfoToSession(actReq, actRes);

    if (isAuthorized(actReq, actRes)) {
      // Allow filter chaining
      try {
        filterChain.doFilter(actReq, actRes);
      } catch (IOException e) {
        log.error("Failed to doFilterChain", e);
      } catch (PortletException e) {
        log.error("Failed to doFilterChain", e);
      }
    }
    log.debug("Finished actionPhase filterChaining");

  }

  /**
   * This method does the actual work.
   * 
   * After checking that user has remoteUserId (is signed in portal) this method
   * gets userData from UserInfoService interface and adds it to UserInfo object
   * which is stored into portlet session
   * 
   * @param request
   */
  private void addUserInfoToSession(PortletRequest request, PortletResponse response) {

    UserInfo userInfo = getUserInfo(request);

    if (userInfo == null) {

      if (log.isDebugEnabled()) {
        log.debug("Couldn't fetch user info.");
      }

      return;
    }

    request.getPortletSession().setAttribute(UserInfo.KEY_USER_INFO, userInfo);

  }

  private void setAuthenticationURL(PortletRequest request, PortletResponse response, UserInfo userInfo) {

    if (!(response instanceof RenderResponse)) {
      log.warn("PortletResponse is not instance of RenderResponse. Could not create authentication url.");
      return;
    }

    if (request.getPortletSession().getAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL) == null) {

      String url = authServiceClient.getAuthenticationURL((RenderResponse) response, userInfo.getUid());

      request.getPortletSession().setAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL, url,
          
          PortletSession.PORTLET_SCOPE);

      if (log.isDebugEnabled()) {
        log.debug("Set authentication url to " + url);
      }
    }
  }

  private UserInfo getUserInfo(PortletRequest request) {
    // Fetch userinfo from session or from remote service

    UserInfo ui = (UserInfo) request.getPortletSession(true).getAttribute(UserInfo.KEY_USER_INFO);

    if (ui == null) {

      String uid = request.getRemoteUser();

      if (uid != null) {

    	if ( VETUMA_PROVIDES_INFO  ) {
        	VetumaUserInfo vetuma = authServiceClient.getVetumaUserInfo(request);
        	if ( vetuma != null ) {
        		ui = userInfoService.getUserInfoById( vetuma.getId() );        		
        		if ( ui != null ) {
        			ui.setUid(uid);
        		}
        	}
        } else if (ui == null ) {
        	ui = userInfoService.getUserInfoById(uid);
        }

        if (log.isDebugEnabled()) {
          log.debug("Fetched userinfo " + ui + " from remote service.");
        }

      } else {

        if (log.isDebugEnabled()) {
          log.debug("Could not get remote user. User hasn't signed in.");
        }
      }
    } else {
      if (log.isDebugEnabled()) {
        log.debug("Fetched userinfo " + ui + " from session.");
      }
    }

    return ui;
  }

  @Override
  public void destroy() {

  }

  /**
   * ResourcePhase pre-filter : Get userData with remoteId and add UserInfo-object
   * to portlet session
   */
  public void doFilter(ResourceRequest request, ResourceResponse response, FilterChain chain) throws IOException,
      PortletException {
    log.debug("Entered UserInfo filter on resourcePhase");

    addUserInfoToSession(request, response);

    if (isAuthorized(request, response)) {
      // Allow filter chaining
      try {
        chain.doFilter(request, response);
      } catch (IOException e) {
        log.error("Failed to doFilterChain", e);
      } catch (PortletException e) {
        log.error("Failed to doFilterChain", e);
      }
    }
    log.debug("Finished renderPhase resourcePhase");

  }

  /**
   * EventPhase pre-filter : Get userData with remoteId and add UserInfo-object
   * to portlet session
   */
  public void doFilter(EventRequest request, EventResponse response, FilterChain chain) throws IOException,
      PortletException {
    log.debug("Entered UserInfo filter on eventPhase");

    addUserInfoToSession(request, response);

    if (isAuthorized(request, response)) {
      // Allow filter chaining
      try {
        chain.doFilter(request, response);
      } catch (IOException e) {
        log.error("Failed to doFilterChain", e);
      } catch (PortletException e) {
        log.error("Failed to doFilterChain", e);
      }
    }
    log.debug("Finished eventPhase filterChaining");

  }

  private boolean isLoggedIn(UserInfo ui, PortletRequest request, PortletResponse response) {

    if (ui == null) {
      return false;
    }

    if (ui.isStrongAuthenticationEnabled()) {
      if (!authServiceClient.hasStrongAuthentication(request, ui.getPic())) {
        setAuthenticationURL(request, response, ui);
        return false;
      }
      return true;
    }
    
    return StringUtils.isNotEmpty(ui.getPic());
  }

  private boolean isAuthorized(PortletRequest request, PortletResponse response) throws IOException,
  PortletException {
    
    UserInfo ui = getUserInfo(request);
    
    if (!isLoggedIn(ui, request, response)) {
   
      PortletRequestDispatcher prd = filterConfig.getPortletContext().getRequestDispatcher(
          "/WEB-INF/jsp/authenticate.jsp");

      if ( ui != null && ui.isStrongAuthenticationEnabled() ) {
        String authenticationURL = (String)request.getPortletSession().getAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL);
        
        if ( authenticationURL == null ) {
          authenticationURL = "";
        }
        
        request.setAttribute("authenticationURL", authenticationURL );
      }
      
      prd.include(request, response);
    
      return false;
    }
    
    return true;
  }

}
