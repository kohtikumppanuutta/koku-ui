package fi.arcusys.koku.palvelut.util;

import static fi.arcusys.koku.util.Constants.INTALIO_GROUP_PREFIX;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.arcusys.koku.intalio.TaskHandle;


public class TokenResolver {

	private static final Logger LOG = LoggerFactory.getLogger(TokenResolver.class);
	private static final String TOKEN_SESSION_PARAMETER = "intalioParticipantToken";
	
	
	public TokenResolver() {
		
	}
	
	/*
	 * Fetches a participant token via a web service call for the authentication
	 * of the current user.
	 * 
	 * @param request The current user is received from the remoteUser
	 * -attribute of a PortletRequest -object.
	 */
	public String getAuthenticationToken(PortletRequest request) {		
		PortletSession session = request.getPortletSession(true);
		String token = (String) session.getAttribute(TOKEN_SESSION_PARAMETER);
		if (StringUtils.isEmpty(token)) {
			try {
				final String username = request.getUserPrincipal().getName();
				// TODO: Resolve userpassword somehow!
				final String password = "test";
				token = getAuthenticationToken(username, password);
				session.setAttribute(TOKEN_SESSION_PARAMETER, token);
			} catch (Exception e) {
				LOG.error("Autenthication failed while trying to resolve Intalio Token. See errorMsg: ",e);
			}
		}
		return token;
	}

	/*
	 * Fetches a participant token using the TokenClient for the specified user.
	 */
	private String getAuthenticationToken(String userName, String password) {
		LOG.debug("Getting participant token via TokenClient for user: " + userName);
		String token = null;
		TaskHandle handle = new TaskHandle();
		// Magic password! Fix also TaskManagerController magic password when possible.
		token =  handle.getTokenByUser(INTALIO_GROUP_PREFIX + userName, "test");
		if (token == null) {
			LOG.error("Coulnd't find Intalio token for user: '"+userName+"'");
		}
		return token;
	}
	
}
