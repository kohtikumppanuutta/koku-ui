package fi.arcusys.koku.users;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
			LOG.error("Failed to create UsersAndGroupsService WSDL url! Given URL address is not valid!");
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
	
	public String getKunpoNameByUserUid(String userUid) {
		return service.getUsersAndGroupsServicePort().getKunpoNameByUserUid(userUid);
	}
	
	public String getLooraNameByUserUid(String userUid) {
		return service.getUsersAndGroupsServicePort().getLooraNameByUserUid(userUid);
	}
	
	public KokuUser getUserInfo(String userUid) {
		User user = service.getUsersAndGroupsServicePort().getUserInfo(userUid);
		if (user != null) {
			return new KokuUser(user);
		} else {
			return null;
		}
	}	
	
	public List<KokuChild> getUsersChildren(String userUid) {
		List<KokuChild> childs = new ArrayList<KokuChild>();
		List<ChildWithHetu> childrens = service.getUsersAndGroupsServicePort().getUsersChildren(userUid);
		for (ChildWithHetu child : childrens) {
			childs.add(new KokuChild(child));
		}
		return childs;
	}

	public List<KokuUser> getUsersByGroupUid(String groupUid) {
		List<KokuUser> kokuUsers = new ArrayList<KokuUser>();
		List<User> users = service.getUsersAndGroupsServicePort().getUsersByGroupUid(groupUid);
		for (User user : users) {
			kokuUsers.add(new KokuUser(user));
		}
		return kokuUsers;
	}
		
	public KokuChild getChildInfo(String childUid) {
		Child child = service.getUsersAndGroupsServicePort().getChildInfo(childUid);
		if (child != null) {
			return new KokuChild(child); 
		} else {
			return null;
		}
	}	
	
	public List<KokuChild> searchChildren(String searchString, int limit) {
		List<Child> childs = service.getUsersAndGroupsServicePort().searchChildren(searchString, limit);
		List<KokuChild> kokuChilds = new ArrayList<KokuChild>();
		for (Child child : childs) {
			kokuChilds.add(new KokuChild(child));
		}
		return kokuChilds;
	}
	
	public void searchGroups(String searchString, int limit) {
		List<Group> groups = service.getUsersAndGroupsServicePort().searchGroups(searchString, limit);
		List<KokuGroup> kokuGroup = new ArrayList<KokuGroup>();
		for (Group group : groups) {
			kokuGroup.add(new KokuGroup(group));
		}
	}

	public List<KokuUser> searchUsers(String searchString, int limit) {
		List<User> users = service.getUsersAndGroupsServicePort().searchUsers(searchString, limit);
		List<KokuUser> kokuUsers = new ArrayList<KokuUser>();
		for (User user : users) {
			kokuUsers.add(new KokuUser(user));
		}
		return kokuUsers;
	}
	
	
	public KokuUser loginKunpo(String kunpoUsername, String hetu) {
		User user = service.getUsersAndGroupsServicePort().loginByKunpoNameAndSsn(kunpoUsername, hetu);
		if (user != null) {
			return new KokuUser(user);
		} else {
			return null;
		}
	}
	
	public KokuUser loginLoora(String looraUsername, String hetu) {
		User user = service.getUsersAndGroupsServicePort().loginByLooraNameAndSsn(looraUsername, hetu);
		if (user != null) {
			return new KokuUser(user);
		} else {
			return null;
		}
	}
	
	public String getKunpoUserUidByHetu(String hetu) {
		return service.getUsersAndGroupsServicePort().getUserUidByKunpoSsn(hetu);
	}
	
	public String getLooraUserUidByUsername(String username) {
		return service.getUsersAndGroupsServicePort().getUserUidByLooraName(username);
	}
	
}
