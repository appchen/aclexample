package com.security.acldoc.dao;

import com.security.acldoc.bean.AbstractSecuredEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
@Slf4j
@Repository
public class SecurityACLDAO {
//	private static Logger logger = LoggerFactory.getLogger(SecurityACLDAO.class);
    @Autowired
    private MutableAclService mutableAclService;

	public SecurityACLDAO() {
	}

	@Transactional(readOnly = false)
    public void addPermission(AbstractSecuredEntity element, Sid recipient, Permission permission) {
        MutableAcl acl;
        //ObjectIdentity oid = new ObjectIdentityImpl(AbstractSecuredEntity.class, element.getId());
        ObjectIdentity oid = new ObjectIdentityImpl(element);

        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }

        acl.insertAce(acl.getEntries().size(), permission, recipient, true);
        mutableAclService.updateAcl(acl);

        log.debug("Added permission " + permission + " for Sid " + recipient + " contact " + element);
    }

	@Transactional(readOnly = false)
    public boolean addAccessControlEntry(AbstractSecuredEntity element, Sid recipient, Permission permission) {
		Assert.notNull(element, "AbstractSecuredEntity required");
		Assert.notNull(recipient, "recipient required");
		Assert.notNull(permission, "permission required");

        MutableAcl acl;
        //ObjectIdentity oid = new ObjectIdentityImpl(AbstractSecuredEntity.class, element.getId());
        ObjectIdentity oid = new ObjectIdentityImpl(element);

        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }

        /*
         * handle duplicate ACL entries
         * http://forum.springsource.org/showthread.php?73022-Should-AclImpl-allow-duplicate-permissions
         */
        if(doesACEExists(element, recipient, permission)) {
            log.debug("ACE already exists, element:" + element.getId() + ", Sid:" + recipient + ", permission:" + permission);
        } else {
            acl.insertAce(acl.getEntries().size(), permission, recipient, true);
            mutableAclService.updateAcl(acl);
            log.debug("Added permission " + permission + " for Sid " + recipient + " contact " + element);
        }
		return true;
    }

	/*
	 * Check if ACE - Access Control Entry exists already
	 */
	public boolean doesACEExists(AbstractSecuredEntity element, Sid recipient, Permission permission) {
		boolean result = false;
        ObjectIdentity oid = new ObjectIdentityImpl(element);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
		
        List<AccessControlEntry> entries = acl.getEntries();
        for(AccessControlEntry ace : entries) {
            if (ace.getSid().equals(recipient) && ace.getPermission().equals(permission)) {
                //result = true;
                return true;
            }
        }
		return result;
	}
	
	@Transactional(readOnly = false)
    public void deletePermission(AbstractSecuredEntity element) {
        // Delete the ACL information as well
        //ObjectIdentity oid = new ObjectIdentityImpl(AbstractSecuredEntity.class, element.getId());
        ObjectIdentity oid = new ObjectIdentityImpl(element);
        mutableAclService.deleteAcl(oid, false);
    }

	@Transactional(readOnly = false)
    public boolean deleteAccessControlEntry(AbstractSecuredEntity element, Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(element);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
		
        List<AccessControlEntry> entries = acl.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(recipient) && entries.get(i).getPermission().equals(permission)) {
                acl.deleteAce(i);
            }
        }

        mutableAclService.updateAcl(acl);

        if (log.isDebugEnabled()) {
            log.debug("Deleted Permission:" + permission + " for recipient: "+ recipient + ", for object: " + element);
        }
        
		return true;
    }

	public MutableAclService getMutableAclService() {
		return mutableAclService;
	}

	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}	
}
