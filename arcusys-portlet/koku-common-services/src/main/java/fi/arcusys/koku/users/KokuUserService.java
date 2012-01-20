package fi.arcusys.koku.users;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.user.usersandgroupsservice.Child;
import fi.arcusys.koku.user.usersandgroupsservice.ChildWithHetu;
import fi.arcusys.koku.user.usersandgroupsservice.Group;
import fi.arcusys.koku.user.usersandgroupsservice.User;
import fi.arcusys.koku.user.usersandgroupsservice.UsersAndGroupsService_Service;
import fi.koku.settings.KoKuPropertiesUtil;

/**
 * Retrieves userId by given portal username
 * 
 * @author Toni Turunen
 *
 */
public class KokuUserService {
	private static final Logger LOG = Logger.getLogger(KokuUserService.class);		
	public static final URL USER_SERVICE_WSDL_LOCATION;
	
	static {
		try {
			LOG.info("UsersAndGroupsService WSDL location: " + KoKuPropertiesUtil.get("UsersAndGroupsService"));
			USER_SERVICE_WSDL_LOCATION =  new URL(KoKuPropertiesUtil.get("UsersAndGroupsService"));
		} catch (MalformedURLException e) {
			LOG.error("Failed to create UsersAndGroupsService WSDL url! Given URL address is not valid! Address: '"+KoKuPropertiesUtil.get("UsersAndGroupsService")+"'");
			throw new ExceptionInInitializerError(e);
		}
	}

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
	public String getUserUidByKunpoName(String username) throws KokuServiceException {
		try {
			return service.getUsersAndGroupsServicePort().getUserUidByKunpoName(username);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUserUidByKunpoName failed. username: '"+username+"'", e);
		}
	}
	
	/**
	 * Get user userId by username from employee portal
	 * 
	 * @param username
	 * @return userId
	 */
	public String getUserUidByLooraName(String username) throws KokuServiceException {
		try {
			return service.getUsersAndGroupsServicePort().getUserUidByLooraName(username);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUserUidByLooraName failed. username: '"+username+"'", e);
		}
	}	
	
	public String getKunpoNameByUserUid(String userUid) throws KokuServiceException {
		try {
			return service.getUsersAndGroupsServicePort().getKunpoNameByUserUid(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getKunpoNameByUserUid failed. userUid: '"+userUid+"'", e);
		}
	}
	
	public String getLooraNameByUserUid(String userUid) throws KokuServiceException {
		try {
			return service.getUsersAndGroupsServicePort().getLooraNameByUserUid(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getLooraNameByUserUid failed. userUid: '"+userUid+"'", e);
		}
	}
	
	public KokuUser getUserInfo(String userUid) throws KokuServiceException {
		try {
			User user = service.getUsersAndGroupsServicePort().getUserInfo(userUid);
			if (user != null) {
				return new KokuUser(user);
			} else {
				return null;
			}
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUserInfo failed. userUid: '"+userUid+"'", e);
		}
	}	
	
	public List<KokuChild> getUsersChildren(String userUid) throws KokuServiceException {
		try {
			List<KokuChild> childs = new ArrayList<KokuChild>();
			List<ChildWithHetu> childrens = service.getUsersAndGroupsServicePort().getUsersChildren(userUid);
			for (ChildWithHetu child : childrens) {
				childs.add(new KokuChild(child));
			}
			return childs;
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUsersChildren failed. userUid: '"+userUid+"'", e);
		}	
	}

	public List<KokuUser> getUsersByGroupUid(String groupUid) throws KokuServiceException {
		try {
			List<KokuUser> kokuUsers = new ArrayList<KokuUser>();
			List<User> users = service.getUsersAndGroupsServicePort().getUsersByGroupUid(groupUid);
			for (User user : users) {
				kokuUsers.add(new KokuUser(user));
			}
			return kokuUsers;
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUsersByGroupUid failed. groupUid: '"+groupUid+"'", e);
		}	
	}
		
	public KokuChild getChildInfo(String childUid) throws KokuServiceException {
		try {
			Child child = service.getUsersAndGroupsServicePort().getChildInfo(childUid);
			if (child != null) {
				return new KokuChild(child); 
			} else {
				return null;
			}
		} catch(RuntimeException e) {
			throw new KokuServiceException("getChildInfo failed. childUid: '"+childUid+"'", e);
		}	
	}
	
	public List<KokuChild> searchChildren(String searchString, int limit) throws KokuServiceException {
		try {
			List<Child> childs = service.getUsersAndGroupsServicePort().searchChildren(searchString, limit);
			List<KokuChild> kokuChilds = new ArrayList<KokuChild>();
			for (Child child : childs) {
				kokuChilds.add(new KokuChild(child));
			}
			return kokuChilds;
		} catch(RuntimeException e) {
			throw new KokuServiceException("searchChildren failed. searchString: '"+searchString+"'", e);
		}	
	}
	
	public void searchGroups(String searchString, int limit) throws KokuServiceException {
		try {
			List<Group> groups = service.getUsersAndGroupsServicePort().searchGroups(searchString, limit);
			List<KokuGroup> kokuGroup = new ArrayList<KokuGroup>();
			for (Group group : groups) {
				kokuGroup.add(new KokuGroup(group));
			}
		} catch(RuntimeException e) {
			throw new KokuServiceException("searchGroups failed. searchString: '"+searchString+"'", e);
		}	
	}

	public List<KokuUser> searchUsers(String searchString, int limit) throws KokuServiceException {
		try {
			List<User> users = service.getUsersAndGroupsServicePort().searchUsers(searchString, limit);
			List<KokuUser> kokuUsers = new ArrayList<KokuUser>();
			for (User user : users) {
				kokuUsers.add(new KokuUser(user));
			}
			return kokuUsers;
		} catch(RuntimeException e) {
			throw new KokuServiceException("searchUsers failed. searchString: '"+searchString+"'", e);
		}	
	}	
	
	public KokuUser loginKunpo(String kunpoUsername, String hetu) throws KokuServiceException {
		try {
			User user = null;
			user = service.getUsersAndGroupsServicePort().loginByKunpoNameAndSsn(kunpoUsername, hetu);
			if (user != null) {
				return new KokuUser(user);
			} else {
				return null;
			}
		} catch(RuntimeException e) {
			throw new KokuServiceException("loginKunpo failed. kunpoUsername: '"+kunpoUsername+"' hetu: '"+hetu+"'", e);
		}	
	}
	
	public KokuUser loginLoora(String looraUsername, String hetu) throws KokuServiceException {
		try {
			User user = null;
			user = service.getUsersAndGroupsServicePort().loginByLooraNameAndSsn(looraUsername, hetu);
			if (user != null) {
				return new KokuUser(user);
			} else {
				return null;
			}
		} catch(RuntimeException e) {
			throw new KokuServiceException("loginLoora failed. looraUserName: '"+looraUsername+"' hetu: '"+hetu+"'", e);
		}	
	}
	
	public String getKunpoUserUidByHetu(String hetu) throws KokuServiceException {
		try {
			return service.getUsersAndGroupsServicePort().getUserUidByKunpoSsn(hetu);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getKunpoUserUidByHetu failed. hetu: '"+hetu+"'", e);
		}	
	}
	
	public String getLooraUserUidByUsername(String username) throws KokuServiceException {
		try {
			return service.getUsersAndGroupsServicePort().getUserUidByLooraName(username);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getLooraUserUidByUsername failed. username: '"+username+"'", e);
		}
	}
	
}
