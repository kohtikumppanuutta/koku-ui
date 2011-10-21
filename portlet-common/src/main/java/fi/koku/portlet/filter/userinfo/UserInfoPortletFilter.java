package fi.koku.portlet.filter.userinfo;

/**
 * Portlet filter that uses portal remoteUserId to retrieve actual user from External Webservice.
 * Local UserInfo object is created using external userdata and after that its stored in portlet session
 * and can be accessed with KEY_USER_INFO -key
 */

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

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

public class UserInfoPortletFilter implements RenderFilter, ActionFilter {

  private static final Logger log = LoggerFactory.getLogger(UserInfoPortletFilter.class);
  private UserInfoService userInfoService = null;

  private AuthenticationServiceClient authServiceClient = null;

  /**
   * Initialize portlet filter.
   */
  public void init(FilterConfig filterConfig) {

    log.debug("Initializing portlet: authImplClassName=" + UserInfoConstants.AUTH_IMPL_CLASS_NAME);

    // Load configured class
    try {

      userInfoService = (UserInfoService) this.getClass().getClassLoader()
          .loadClass(UserInfoConstants.AUTH_IMPL_CLASS_NAME).newInstance();

      String context = KoKuPropertiesUtil.get("environment.name");
      
      if ("kunpo".equals(context)) {
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
          }};
          
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
  public void doFilter(RenderRequest request, RenderResponse response, FilterChain filterChain) {
    log.debug("Entered UserInfo filter on renderPhase");

    addUserInfoToSession(request, response);

    // Allow filter chaining
    try {
      filterChain.doFilter(request, response);
    } catch (IOException e) {
      log.error("Failed to doFilterChain", e);
    } catch (PortletException e) {
      log.error("Failed to doFilterChain", e);
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

    // Allow filter chaining
    try {
      filterChain.doFilter(actReq, actRes);
    } catch (IOException e) {
      log.error("Failed to doFilterChain", e);
    } catch (PortletException e) {
      log.error("Failed to doFilterChain", e);
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
    
    if (userInfo.isStrongAuthenticationEnabled() && !userInfo.hasStrongAuthentication()) {
      
      if (log.isDebugEnabled()) {
        log.debug("User does not have strongly authenticated session.");
      }      
      
      setStrongAuthentication(request, response, userInfo);
    }
    
    request.getPortletSession().setAttribute(UserInfo.KEY_USER_INFO, userInfo);
    
  }

  private void setStrongAuthentication(PortletRequest request, PortletResponse response, UserInfo userInfo) {

    if (authServiceClient.hasStrongAuthentication(request)) {
      updateUserInfo(request, userInfo);
    } else {
      setAuthenticationURL(request, response, userInfo);
    }
  }

  private void setAuthenticationURL(PortletRequest request, PortletResponse response, UserInfo userInfo) {
    
    if (!(response instanceof RenderResponse)) {
      log.warn("PortletResponse is not instance of RenderResponse. Could not create authentication url.");
      return;
    }
    
    if (request.getPortletSession().getAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL) == null) {
      
      String url = authServiceClient.getAuthenticationURL((RenderResponse) response, userInfo.getUid()); 
      
      request.getPortletSession().setAttribute(UserInfo.KEY_VETUMA_AUTHENTICATION_URL,
          url, PortletSession.PORTLET_SCOPE);
      
      if (log.isDebugEnabled()) {
        log.debug("Set authentication url to " + url);
      }
    }
  }

  private void updateUserInfo(PortletRequest request, UserInfo userInfo) {

    VetumaUserInfo ticketString = authServiceClient.getVetumaUserInfo(request);

    if (userInfo.getPic().equals(ticketString.getId())) {

      userInfo.setStrongAuthentication(true);

      if (log.isDebugEnabled()) {
        log.debug("Strong authentication set for user " + userInfo);
      }

    } else {
      log.warn("Pic from UserInfoService does not match Vetuma's id.");
    }

  }

  private UserInfo getUserInfo(PortletRequest request) {
  // Fetch userinfo from session or from remote service
    
    UserInfo ui = (UserInfo)request.getPortletSession(true).getAttribute(UserInfo.KEY_USER_INFO);
    
    if (ui == null) {
      
      String uid = request.getRemoteUser();
      
      if (uid != null) {

        ui = userInfoService.getUserInfoById(uid);
        
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

}