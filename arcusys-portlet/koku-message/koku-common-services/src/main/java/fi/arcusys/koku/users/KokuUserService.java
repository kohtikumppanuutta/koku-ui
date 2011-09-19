package fi.arcusys.koku.users;

import java.net.URL;

import fi.arcusys.koku.user.usersandgroupsservice.UsersAndGroupsService_Service;

/**
 * Retrieves userId by given portal username
 * 
 * @author Toni Turunen
 *
 */
public class KokuUserService {
	
	public final URL USER_SERVICE_WSDL_LOCATION = getClass().getClassLoader().getResource("UserAndGroupsService.wsdl");
	private UsersAndGroupsService_Service service;
	
	/**
	 * Constructor and initialization
	 */
	public KokuUserService() {
		service = new UsersAndGroupsService_Service(USER_SERVICE_WSDL_LOCATION);
	}
	
	/**
	 * Get user userId by username from Citizen portal
	 * 
	 * @param username
	 * @return userId
	 */
	public String getUserUidByKunpoName(String username) {
		return service.getUsersAndGroupsServicePort().getUserUidByKunpoName(username);
	}
	
	/**
	 * Get user userId by username from employee portal
	 * 
	 * @param username
	 * @return userId
	 */
	public String getUserUidByLooraName(String username) {
		return service.getUsersAndGroupsServicePort().getUserUidByLooraName(username);
	}
}
