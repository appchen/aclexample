package com.security.acldoc.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

//@XmlRootElement(name="UserACLRequestSet")
public class UserACLRequestSet {
	List<UserACLRequest> aclList;

	public List<UserACLRequest> getAclList() {
		return aclList;
	}

	public void setAclList(List<UserACLRequest> aclList) {
		this.aclList = aclList;
	}
}