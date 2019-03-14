package com.security.acldoc.bean;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
//@XmlRootElement(name = "Role")
@Table(name = "tb_role")
public class Role extends AbstractSecuredEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * converts to upper case, to make it case-insensitive
	 */
	@Column(unique = true, nullable = false)
	private String authority;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>();

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority.toUpperCase(Locale.ENGLISH);
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((authority == null) ? 0 : authority.hashCode());
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
		Role other = (Role) obj;
		if (authority == null) {
			if (other.authority != null) {
				return false;
			}
		} else if (!authority.equals(other.authority)) {
			return false;
		}
		return true;
	}
}
