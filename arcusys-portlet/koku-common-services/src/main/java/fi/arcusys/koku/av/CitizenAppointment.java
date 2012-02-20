package fi.arcusys.koku.av;

import fi.arcusys.koku.users.KokuUser;

/**
 * Appointment data model for citizen
 * @author Jinhua Chen
 * Aug 22, 2011
 */
public class CitizenAppointment extends KokuAppointment{

	private Slot slot;
	private String replier;
	private KokuUser replierUser;
	private String replierComment;
	private String targetPersonUid;
    private String targetPersonDisplayName;
	private KokuUser targetPersonUser;

	
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
	 * @return the replierUser
	 */
	public final KokuUser getReplierUser() {
		return replierUser;
	}

	/**
	 * @param replierUser the replierUser to set
	 */
	public final void setReplierUser(KokuUser replierUser) {
		this.replierUser = replierUser;
	}

	/**
     * @return the targetPersonUid
     */
    public String getTargetPersonUid() {
        return targetPersonUid;
    }

    /**
     * @param targetPersonUid the targetPersonUid to set
     */
    public void setTargetPersonUid(String targetPersonUid) {
        this.targetPersonUid = targetPersonUid;
    }

    /**
     * @return the targetPersonDisplayName
     */
    public String getTargetPersonDisplayName() {
        return targetPersonDisplayName;
    }

    /**
     * @param targetPersonDisplayName the targetPersonDisplayName to set
     */
    public void setTargetPersonDisplayName(String targetPersonDisplayName) {
        this.targetPersonDisplayName = targetPersonDisplayName;
    }

    public Slot getSlot() {
		return slot;
	}
	
	public String getReplier() {
		return replier;
	}
	
	public String getReplierComment() {
		return replierComment;
	}
	
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	
	public void setReplier(String replier) {
		this.replier = replier;
	}
	
	public void setReplierComment(String replierComment) {
		this.replierComment = replierComment;
	}
}
