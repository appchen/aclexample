package com.security.acldoc.repositories;

import com.security.acldoc.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "user")
public interface UserEntityRepo extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
