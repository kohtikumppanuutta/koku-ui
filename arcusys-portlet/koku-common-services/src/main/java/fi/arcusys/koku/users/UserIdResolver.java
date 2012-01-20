package fi.arcusys.koku.users;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.util.PortalRole;

public class UserIdResolver {
	
	private static Logger LOG = Logger.getLogger(UserIdResolver.class);

	private KokuUserService userService;
	
	public UserIdResolver() {
		userService = new KokuUserService();
	}

	/**
	 * Returns unique userId by given username. 
	 * If user not found returns null.
	 * 
	 * @param username or null if user not found
	 * @return userId
	 */
	public String getUserId(String username, PortalRole role) {
		String userId = null;
		if (username == null || username.isEmpty()) {
			return null;
		}
	
		try {
			switch (role) {
			case CITIZEN:
				userId = userService.getUserUidByKunpoName(username);
				break;
			case EMPLOYEE:
				userId = userService.getUserUidByLooraName(username);
				break;
			default:
				userId = null;
				break;
			}
		} catch (KokuServiceException e) {
			LOG.error("Failed to get UserUid username: '"+username+"' portalRole: '"+role+"'",e);
		}
		
		if (userId == null) {
			LOG.info("Couldn't find userId by given username: " + username + " PortalRole: " + role);
		}
		return userId;
	}
}
