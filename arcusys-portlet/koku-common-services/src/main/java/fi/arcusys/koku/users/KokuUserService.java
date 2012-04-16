package fi.arcusys.koku.users;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.BindingProvider;

import fi.arcusys.koku.exceptions.KokuServiceException;
import fi.arcusys.koku.user.usersandgroupsservice.Child;
import fi.arcusys.koku.user.usersandgroupsservice.ChildWithHetu;
import fi.arcusys.koku.user.usersandgroupsservice.Group;
import fi.arcusys.koku.user.usersandgroupsservice.User;
import fi.arcusys.koku.user.usersandgroupsservice.UsersAndGroupsService;
import fi.arcusys.koku.user.usersandgroupsservice.UsersAndGroupsService_Service;
import fi.arcusys.koku.util.Properties;

/**
 * Retrieves userId by given portal username
 * 
 * @author Toni Turunen
 *
 */
public class KokuUserService {
	private final UsersAndGroupsService service;
	
	/**
	 * Constructor and initialization
	 */
	public KokuUserService() {
		UsersAndGroupsService_Service serviceInit = new UsersAndGroupsService_Service();
		service = serviceInit.getUsersAndGroupsServicePort();
		((BindingProvider)service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, Properties.USER_SERVICE);
		// SOAPLoggingHandler.addSOAPLogger((BindingProvider)service);
	}
	
	/**
	 * Get user userId by username from Citizen portal
	 * 
	 * @param username
	 * @return userId
	 */
	public String getUserUidByKunpoName(String username) throws KokuServiceException {
		try {
			return service.getUserUidByKunpoName(username);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUserUidByKunpoName failed. username: '"+username+"'", e);
		}
	}
	
	/**
	 * Get user userId by username from employee serviceal
	 * 
	 * @param username
	 * @return userId
	 */
	public String getUserUidByLooraName(String username) throws KokuServiceException {
		try {
			return service.getUserUidByLooraName(username);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getUserUidByLooraName failed. username: '"+username+"'", e);
		}
	}	
	
	public String getKunpoNameByUserUid(String userUid) throws KokuServiceException {
		try {
			return service.getKunpoNameByUserUid(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getKunpoNameByUserUid failed. userUid: '"+userUid+"'", e);
		}
	}
	
	public String getLooraNameByUserUid(String userUid) throws KokuServiceException {
		try {
			return service.getLooraNameByUserUid(userUid);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getLooraNameByUserUid failed. userUid: '"+userUid+"'", e);
		}
	}
	
	public KokuUser getUserInfo(String userUid) throws KokuServiceException {
		try {
			User user = service.getUserInfo(userUid);
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
			List<ChildWithHetu> childrens = service.getUsersChildren(userUid);
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
			List<User> users = service.getUsersByGroupUid(groupUid);
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
			Child child = service.getChildInfo(childUid);
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
			List<Child> childs = service.searchChildren(searchString, limit);
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
			List<Group> groups = service.searchGroups(searchString, limit);
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
			List<User> users = service.searchUsers(searchString, limit);
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
			user = service.loginByKunpoNameAndSsn(kunpoUsername, hetu);
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
			user = service.loginByLooraNameAndSsn(looraUsername, hetu);
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
			return service.getUserUidByKunpoSsn(hetu);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getKunpoUserUidByHetu failed. hetu: '"+hetu+"'", e);
		}	
	}
	
	public String getLooraUserUidByUsername(String username) throws KokuServiceException {
		try {
			return service.getUserUidByLooraName(username);
		} catch(RuntimeException e) {
			throw new KokuServiceException("getLooraUserUidByUsername failed. username: '"+username+"'", e);
		}
	}
	
}
