package fi.arcusys.koku.av;

import fi.arcusys.koku.av.employeeservice.AppointmentUserRejected;

public class KokuAppointmentUserRejected {
	
    private String rejectComment;
    private String userDisplayName;
    private String userUid;
    
    public KokuAppointmentUserRejected() {
    	
    }
    
    public KokuAppointmentUserRejected(AppointmentUserRejected userRejected) {
    	rejectComment = userRejected.getRejectComment();
    	userUid = userRejected.getUserInfo().getUid();
    	userDisplayName = userRejected.getUserInfo().getDisplayName();
    }    
    
	public final String getRejectComment() {
		return rejectComment;
	}
	public final void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}
	public final String getUserDisplayName() {
		return userDisplayName;
	}
	public final void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}
	public final String getUserUid() {
		return userUid;
	}
	public final void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	@Override
	public String toString() {
		return "KokuAppointmentUserRejected [rejectComment=" + rejectComment
				+ ", userDisplayName=" + userDisplayName + ", userUid="
				+ userUid + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((rejectComment == null) ? 0 : rejectComment.hashCode());
		result = prime * result	+ ((userDisplayName == null) ? 0 : userDisplayName.hashCode());
		result = prime * result + ((userUid == null) ? 0 : userUid.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;			
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KokuAppointmentUserRejected other = (KokuAppointmentUserRejected) obj;
		if (rejectComment == null) {
			if (other.rejectComment != null){
				return false;
			}
		} else if (!rejectComment.equals(other.rejectComment)) {
			return false;
		}
		if (userDisplayName == null) {
			if (other.userDisplayName != null) {
				return false;
			}
		} else if (!userDisplayName.equals(other.userDisplayName)) {
			return false;
		}
		if (userUid == null) {
			if (other.userUid != null) {
				return false;
			}
		} else if (!userUid.equals(other.userUid)) {
			return false;
		}
		return true;
	}

}
