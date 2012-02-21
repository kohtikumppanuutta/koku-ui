package fi.arcusys.koku.users;

import java.util.List;

public class KokuUser {
	
	private String firstname;
	private String lastname;
	private String displayname;
	private String phoneNumber;
	private String email;
	
	public KokuUser() {
		
	}
	
	public KokuUser(fi.arcusys.koku.user.usersandgroupsservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public KokuUser(fi.arcusys.koku.kv.requestservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public KokuUser(fi.arcusys.koku.kv.messageservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public KokuUser(fi.arcusys.koku.tiva.employeeservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public KokuUser(fi.arcusys.koku.tiva.citizenservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());			
		}
	}

	public KokuUser(fi.arcusys.koku.av.employeeservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());
		}
	}

	public KokuUser(fi.arcusys.koku.av.citizenservice.User user) {
		if (user != null) {
			setFirstname(user.getFirstname());
			setLastname(user.getLastname());
			setDisplayname(user.getDisplayName());
			setPhoneNumber(user.getPhoneNumber());
			setEmail(user.getEmail());
		}
	}

	public final String getFirstname() {
		return firstname;
	}

	public final void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public final String getLastname() {
		return lastname;
	}

	public final void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public final String getDisplayname() {
		return displayname;
	}

	public final void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public final String getPhoneNumber() {
		return phoneNumber;
	}

	public final void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		this.email = email;
	}
	
	public final String getFullName() {
		if (firstname == null || lastname == null) {
			return null;
		} else {
			return firstname + " " + lastname;			
		}
	}

	@Override
	public String toString() {
		return "KokuUser [firstname=" + firstname
				+ ", lastname=" + lastname + ", displayname=" + displayname
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
		return true;
	}
	
}
