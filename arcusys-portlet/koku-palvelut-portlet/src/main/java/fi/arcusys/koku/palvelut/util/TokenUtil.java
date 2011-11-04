package fi.arcusys.koku.palvelut.util;

import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.intalio.tempo.security.ws.TokenClient;
import org.jasig.cas.client.validation.Assertion;

import fi.arcusys.koku.palvelut.bean.Configuration;


public class TokenUtil {

	private static final String TOKEN_SESSION_PARAMETER = "intalioParticipantToken";

	private static final Log LOG = LogFactory.getLog(TokenUtil.class);
	private static final String DEFAULT_PASSWORD;
	private static final String REALM;

	static {
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle("veera");
		} catch (Exception e) {
			LOG.error("ResourceBundle failure: ", e);
			throw new ExceptionInInitializerError("Error while loading Veera ResourceBundle. ");
		}
		DEFAULT_PASSWORD = rb.getString("password");
		REALM = rb.getString("realm");
	}

	/*
	 * Fetches a participant token via a web service call for the authentication
	 * of the current user.
	 * 
	 * @param request The current user is received from the remoteUser
	 * -attribute of a PortletRequest -object.
	 */
	public static String getAuthenticationToken(PortletRequest request) {
		PortletSession session = request.getPortletSession(true);
		String token = (String) session.getAttribute(TOKEN_SESSION_PARAMETER);
		if (StringUtils.isEmpty(token)) {
			try {
				Assertion assertion = MigrationUtil.getAssertion(request);
				
				if (assertion != null) {
					String serviceURL = MigrationUtil.getServiceURL(request);
					String pgt = assertion.getPrincipal().getProxyTicketFor(serviceURL);
					token = getAuthenticationTokenFromTicket(pgt, serviceURL);
				} else {
					token = getAuthenticationToken(MigrationUtil.getUser(request), MigrationUtil.getUserPassword(request));
				}
				session.setAttribute(TOKEN_SESSION_PARAMETER, token);
			} catch (Exception e) {
				LOG.error(e);
			}
		}
		return token;
	}

	/*
	 * Fetches a participant token using the TokenClient for the specified user.
	 */
	public static String getAuthenticationToken(String userName, String password) {
		LOG.debug("Getting participant token via TokenClient for user: "
				+ userName);
		String token = null;
		TokenClient tokenClient = new TokenClient(Configuration.getTokenServiceEndpoint());
		try {
			token = tokenClient.authenticateUser(userName, password);
		} catch (Exception e) {
			LOG.error("Getting participant token failed for user: " + userName, e);
		}
		return token;
	}
	
	public static String getAuthenticationTokenFromTicket(String ticket, String serviceURL) {
		if (LOG.isDebugEnabled()) {
	 		LOG.debug("Getting participant token via TokenClient for ticket");
		}
		String token = null;
		TokenClient tokenClient = new TokenClient(Configuration.getTokenServiceEndpoint());
		try {
			token = tokenClient.getTokenFromTicket(ticket, serviceURL);
		} catch (Exception e) {
			LOG.error(e);
		}
		return token;
	}

}
