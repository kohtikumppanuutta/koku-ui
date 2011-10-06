package fi.arcusys.koku.users;

import java.util.ArrayList;
import java.util.List;

import fi.arcusys.koku.user.usersandgroupsservice.Child;
import fi.arcusys.koku.user.usersandgroupsservice.User;

public class KokuChild extends KokuUser {

	protected List<User> parents;

	public KokuChild(User user) {
		super(user);
	}
	
	public KokuChild(Child child) {
		super(child);
		parents = child.getParents();
	}
	

    public List<User> getParents() {
        if (parents == null) {
            parents = new ArrayList<User>();
        }
        return this.parents;
    }

	@Override
	public String toString() {
		return "KokuChild [parents=" + parents + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parents == null) ? 0 : parents.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		KokuChild other = (KokuChild) obj;
		if (parents == null) {
			if (other.parents != null)
				return false;
		} else if (!parents.equals(other.parents))
			return false;
		return true;
	}
    
}
