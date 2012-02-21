package fi.arcusys.koku.av;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.users.KokuUser;

/**
 * Slot data model
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class Slot {

    private long appointmentId;
    private int slotNumber;
    private String date;
    private String startTime;
    private String endTime;
    private String location;
    private String comment;
    private boolean approved;
    private KokuUser targetPersonUser;
    private final List<KokuUser> recipientUsers = new ArrayList<KokuUser>();;
    
    
    /* getters */
    public long getAppointmentId() {
    	return appointmentId;
    }
    
    public long getSlotNumber() {
    	return slotNumber;
    }
    
    public String getDate() {
    	return date;
    }
    
    public String getStartTime() {
    	return startTime;
    }
    
    public String getEndTime() {
    	return endTime;
    }
    
    public String getLocation() {
    	return location;
    }
    
    public String getComment() {
    	return comment;
    }
    
    public boolean getApproved() {
    	return approved;
    }
    
    /* setters */
    public void setAppointmentId(long appointmentId) {
    	this.appointmentId = appointmentId;
    }
    
    public void setSlotNumber(int slotNumber) {
    	this.slotNumber = slotNumber;
    }
    
    public void setAppointmentDate(String date) {
    	this.date = date;
    }
    
    public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }
    
    public void setEndTime(String endTime) {
    	this.endTime = endTime;
    }
    
    public void setLocation(String location) {
    	this.location = location;
    }
    
    public void setComment(String comment) {
    	this.comment = comment;
    }
    
    public void setApproved(boolean approved) {
    	this.approved = approved;
    }

	/**
	 * @return the targetPersonUser
	 */
	public final KokuUser getTargetPersonUser() {
		return targetPersonUser;
	}

	/**
	 * @param targetPersonUser the targetPersonUser to set
	 */
	public final void setTargetPersonUser(KokuUser targetPersonUser) {
		this.targetPersonUser = targetPersonUser;
	}

	/**
	 * @return the recipientUsers
	 */
	public final List<KokuUser> getRecipientUsers() {
		return recipientUsers;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (appointmentId ^ (appointmentId >>> 32));
		result = prime * result + (approved ? 1231 : 1237);
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((recipientUsers == null) ? 0 : recipientUsers.hashCode());
		result = prime * result + slotNumber;
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime
				* result
				+ ((targetPersonUser == null) ? 0 : targetPersonUser.hashCode());
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
		Slot other = (Slot) obj;
		if (appointmentId != other.appointmentId) {
			return false;
		}
		if (approved != other.approved) {
			return false;
		}
		if (comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!comment.equals(other.comment)) {
			return false;
		}
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		if (endTime == null) {
			if (other.endTime != null) {
				return false;
			}
		} else if (!endTime.equals(other.endTime)) {
			return false;
		}
		if (location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!location.equals(other.location)) {
			return false;
		}
		if (recipientUsers == null) {
			if (other.recipientUsers != null) {
				return false;
			}
		} else if (!recipientUsers.equals(other.recipientUsers)) {
			return false;
		}
		if (slotNumber != other.slotNumber) {
			return false;
		}
		if (startTime == null) {
			if (other.startTime != null) {
				return false;
			}
		} else if (!startTime.equals(other.startTime)) {
			return false;
		}
		if (targetPersonUser == null) {
			if (other.targetPersonUser != null) {
				return false;
			}
		} else if (!targetPersonUser.equals(other.targetPersonUser)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Slot [appointmentId=" + appointmentId + ", slotNumber="
				+ slotNumber + ", date=" + date + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", location=" + location
				+ ", comment=" + comment + ", approved=" + approved
				+ ", targetPersonUser=" + targetPersonUser
				+ ", recipientUsers=" + recipientUsers + "]";
	}

	
    

}
