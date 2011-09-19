package fi.arcusys.koku.users;

import fi.arcusys.koku.util.PortalRole;

public class UserIdResolver {
	
	private KokuUserService userService;
	
	public UserIdResolver() {
		userService = new KokuUserService();
	}

	/**
	 * Returns unique userId by given username 
	 * 
	 * @param username
	 * @return userId
	 */
	public String getUserId(String username, PortalRole role) {
		String userId = null;
		if (username == null) {
			return null;
		}
	
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
		
		if (userId == null) {
			throw new IllegalArgumentException("Couldn't find userId by given username: " + username + " PortalRole: " + role);
		}
		
		return userId;
	}
	

}
