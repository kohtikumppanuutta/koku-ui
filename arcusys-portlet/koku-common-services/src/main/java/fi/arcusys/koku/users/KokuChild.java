package fi.arcusys.koku.users;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.user.usersandgroupsservice.Child;
import fi.arcusys.koku.user.usersandgroupsservice.ChildWithHetu;
import fi.arcusys.koku.user.usersandgroupsservice.User;

/**
 * KokuChild
 * 
 * Citizen child
 * 
 * @author Toni Turunen
 *
 */
public class KokuChild extends KokuUser {

	private List<User> parents;
	private String hetu;
	

	public KokuChild(User user) {
		super(user);
	}
	
	public KokuChild(Child child) {
		super(child);
		parents = child.getParents();
	}
	
	public KokuChild(ChildWithHetu child) {
		this((Child)child);
		hetu = child.getHetu();
	}	

    public String getHetu() {
		return hetu;
	}

	public void setHetu(String hetu) {
		this.hetu = hetu;
	}

	public void setParents(List<User> parents) {
		this.parents = parents;
	}

	public List<User> getParents() {
        if (parents == null) {
            parents = new ArrayList<User>();
        }
        return this.parents;
    }

	@Override
	public String toString() {
		return "KokuChild [parents=" + parents + ", hetu=" + hetu + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((hetu == null) ? 0 : hetu.hashCode());
		result = prime * result + ((parents == null) ? 0 : parents.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		KokuChild other = (KokuChild) obj;
		if (hetu == null) { 
			if (other.hetu != null) {
				return false;				
			}
		} else if (!hetu.equals(other.hetu)) {
			return false;
		}
		if (parents == null) { 
			if (other.parents != null) {
				return false;
			}
		} else if (!parents.equals(other.parents)) {
			return false;
		}
		return true;
	}
    
}
