package fi.arcusys.koku;

import org.springframework.context.support.ResourceBundleMessageSource;

import fi.arcusys.koku.users.KokuUserService;
import fi.arcusys.koku.util.PortalRole;

public abstract class AbstractHandle {
	
	private ResourceBundleMessageSource messageSource;
//	private PortalRole portalRole;
//	private KokuUserService userService;
//	
//	@SuppressWarnings("unused")
//	private AbstractHandle() {
//		// Prevent instantiating without portalRole property
//	}
//	
//	/** 
//	 * Default constructor for abstract class 
//	 */
//	public AbstractHandle(PortalRole portalRole) {
//		if (portalRole == null) {
//			throw new IllegalArgumentException("PortalRole argument can't be null");
//		}
//		this.portalRole = portalRole;
//		userService = new KokuUserService();
//	}
//	
//	/**
//	 * Returns unique userId by given username 
//	 * 
//	 * @param username
//	 * @return userId
//	 */
//	public String getUserId(String username) {
//		String userId = null;
//	
//		switch (getPortalRole()) {
//		case CITIZEN:
//			userId = userService.getUserUidByKunpoName(username);
//			break;
//		case EMPLOYEE:
//			userId = userService.getUserUidByLooraName(username);
//			break;
//		default:
//			userId = null;
//			break;
//		}
//		
//		if (userId == null) {
//			throw new IllegalArgumentException("Couldn't find userId by given username: " + username + " PortalRole: " + getPortalRole().toString());
//		}
//		
//		return userId;
//	}
//	
//	public final PortalRole getPortalRole() {
//		return portalRole;
//	}
//	
//	public final void setPortalRole(PortalRole portalRole) {
//		this.portalRole = portalRole;
//	}
	
	public final ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public final void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
