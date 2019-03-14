package com.security.acldoc.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
@Component("myPermissionEvaluator")
public class MyPermissionEvaluator implements PermissionEvaluator {


    @Override
    public boolean hasPermission(Authentication authentication,

                                 Object targetDomainObject, Object permission) {

        if ("user".equals(targetDomainObject)) {

            return this.hasPermission(authentication, permission);

        }

        return false;

    }


    /**
     * 总是认为有权限
     */

    @Override
    public boolean hasPermission(Authentication authentication,

                                 Serializable targetId, String targetType, Object permission) {

        return true;

    }


    /**
     * 简单的字符串比较，相同则认为有权限
     */

    private boolean hasPermission(Authentication authentication, Object permission) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {

            if (authority.getAuthority().equals(permission)) {

                return true;

            }

        }

        return false;

    }


}