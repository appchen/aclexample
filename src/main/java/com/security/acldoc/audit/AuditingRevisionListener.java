package com.security.acldoc.audit;

public class AuditingRevisionListener /*implements RevisionListener*/ {

	/*@Override
	public void newRevision(Object revisionEntity) {
		AuditRevision auditedRevision = (AuditRevision) revisionEntity;
		String userName = SecurityUtil.getUsername();
		*//* possible approach to get IP address of the user
		- http://stackoverflow.com/questions/12786123/ip-filter-using-spring-security
		- http://forum.springsource.org/showthread.php?18071-pass-ip-address-to-authentication-provider
		*//*
		
		WebAuthenticationDetails auth = (WebAuthenticationDetails)
				SecurityContextHolder.getContext().getAuthentication().getDetails();
		if(auth != null) {
			String ipAddress = auth.getRemoteAddress();
			auditedRevision.setIpAddress(ipAddress);
		}
		
		auditedRevision.setUsername(userName);
	}*/
}