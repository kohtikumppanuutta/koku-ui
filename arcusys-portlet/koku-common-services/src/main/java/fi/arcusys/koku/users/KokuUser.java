package fi.arcusys.koku.users;

public class KokuUser {
	
	private String uid;
	private String firstname;
	private String lastname;
	private String displayname;
	private String phoneNumber;
	private String email;
	
	public KokuUser() {
		
	}
	
	public KokuUser(fi.arcusys.koku.user.usersandgroupsservice.User user) {
		if (user != null) {
			setUid(user.getUid());
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}


	public KokuUser(fi.arcusys.koku.kv.requestservice.User user) {
		if (user != null) {
			setUid(user.getUid());
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public KokuUser(fi.arcusys.koku.kv.messageservice.User user) {
		if (user != null) {
			setUid(user.getUid());
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public String getUid() {
		return uid;
	}


	public void setUid(String uid) {
		this.uid = uid;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getDisplayname() {
		return displayname;
	}


	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFullName() {
		return firstname + " " + lastname;
	}


	@Override
	public String toString() {
		return "KokuUser [uid=" + uid + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", displayname=" + displayname
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((displayname == null) ? 0 : displayname.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result
				+ ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		KokuUser other = (KokuUser) obj;
		if (displayname == null) {
			if (other.displayname != null) {
				return false;
			}
		} else if (!displayname.equals(other.displayname)) {
			return false;
		}
		if (email == null) {
			if (other.email != null) {
				return false;
			}
		} else if (!email.equals(other.email)) {
			return false;
		}
		if (firstname == null) {
			if (other.firstname != null) {
				return false;
			}
		} else if (!firstname.equals(other.firstname)) {
			return false;
		}
		if (lastname == null) {
			if (other.lastname != null) {
				return false;
			}
		} else if (!lastname.equals(other.lastname)) {
			return false;
		}
		if (phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		} else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		if (uid == null) {
			if (other.uid != null) {
				return false;
			}
		} else if (!uid.equals(other.uid)) {
			return false;
		}
		return true;
	}
	
}
