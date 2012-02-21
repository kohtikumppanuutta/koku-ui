package fi.arcusys.koku.av;

import fi.arcusys.koku.av.employeeservice.AppointmentUserRejected;
import fi.arcusys.koku.users.KokuUser;

public class KokuAppointmentUserRejected {
	
    private String rejectComment;
    private KokuUser user;
    
    public KokuAppointmentUserRejected() {
    	
    }
    
    public KokuAppointmentUserRejected(AppointmentUserRejected userRejected) {
    	rejectComment = userRejected.getRejectComment();    	
    	user = new KokuUser(userRejected.getUserInfo());
    }   
    
	/**
	 * @return the user
	 */
	public final KokuUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public final void setUser(KokuUser user) {
		this.user = user;
	}

	public final String getRejectComment() {
		return rejectComment;
	}
	public final void setRejectComment(String rejectComment) {
		this.rejectComment = rejectComment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KokuAppointmentUserRejected [rejectComment=" + rejectComment
				+ ", user=" + user + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rejectComment == null) ? 0 : rejectComment.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		KokuAppointmentUserRejected other = (KokuAppointmentUserRejected) obj;
		if (rejectComment == null) {
			if (other.rejectComment != null) {
				return false;
			}
		} else if (!rejectComment.equals(other.rejectComment)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}
	
	

}
