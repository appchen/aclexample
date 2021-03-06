package com.security.acldoc.repositories;

import com.security.acldoc.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "role")
public interface RoleEntityRepo extends JpaRepository<Role, Long> {

}
