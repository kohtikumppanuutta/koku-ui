package fi.arcusys.koku.users;

import fi.arcusys.koku.user.usersandgroupsservice.Group;

/**
 * KokuGroup
 * 
 * @author Toni Turunen
 *
 */

public class KokuGroup {

	private String groupName;
	private String groupUid;
	
	public KokuGroup(Group group) {
		setGroupName(group.getGroupName());
		setGroupUid(group.getGroupUid());
	}	
	
	public String getGroupName() {
		return groupName;
	}
	public final void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupUid() {
		return groupUid;
	}
	public final void setGroupUid(String groupUid) {
		this.groupUid = groupUid;
	}
	
	@Override
	public String toString() {
		return "KokuGroup [groupName=" + groupName + ", groupUid=" + groupUid
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result
				+ ((groupUid == null) ? 0 : groupUid.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;		
		}
		KokuGroup other = (KokuGroup) obj;
		if (groupName == null) {
			if (other.groupName != null) {
				return false;
			}
		} else if (!groupName.equals(other.groupName)) {
			return false;
		}
		if (groupUid == null) {
			if (other.groupUid != null) {
				return false;
			}
		} else if (!groupUid.equals(other.groupUid)) {
			return false;
		}
		return true;
	}
	
}
