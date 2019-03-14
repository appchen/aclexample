package com.security.acldoc.handlers;

import com.security.acldoc.bean.AbstractSecuredEntity;
import com.security.acldoc.bean.Book;
import com.security.acldoc.dao.SecurityACLDAO;
import com.security.acldoc.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RepositoryEventHandler
public class BookHandler extends AbstractRepositoryEventListener<Book> {
//    private static Logger log = LoggerFactory.getLogger(BookHandler.class);

    @Autowired
    private SecurityACLDAO securityACLDAO;

    @Override
    protected void onAfterCreate(Book book) {
        log.debug("afterCreate:{}", book.toString());
        addACL(book);
    }

    @Override
    protected void onAfterSave(Book book) {
        log.debug("afterSave:{}", book.toString());
    }

    @Override
    protected void onAfterDelete(Book book) {
        removeACL(book);
    }

   /* @HandleAfterCreate
    public void afterCreate(Book book) {

    }

    @HandleAfterSave
    public void handleAfterSave(Book book) {
        log.debug("afterSave:{}", book.toString());
    }

    @HandleAfterDelete
    public void handleAfterDelete(Book book) {
        removeACL(book);
    }*/

    private void addACL(AbstractSecuredEntity type) {
        if(type != null) {
            securityACLDAO.addPermission(type, new PrincipalSid(SecurityUtil.getUsername()), BasePermission.ADMINISTRATION);
            securityACLDAO.addPermission(type, new PrincipalSid(SecurityUtil.getUsername()), BasePermission.READ);
            securityACLDAO.addPermission(type, new PrincipalSid(SecurityUtil.getUsername()), BasePermission.WRITE);
            securityACLDAO.addPermission(type, new PrincipalSid(SecurityUtil.getUsername()), BasePermission.DELETE);

            securityACLDAO.addPermission(type, new GrantedAuthoritySid("ROLE_ADMIN"), BasePermission.ADMINISTRATION);
        }
    }

    private void removeACL(AbstractSecuredEntity type) {
        //TBD
    }
}
