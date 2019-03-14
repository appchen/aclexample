package com.security.acldoc.repositories;

import com.security.acldoc.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(path = "book")
public interface BookRepository extends JpaRepository<Book, Long> {
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#book, 'write')")
	<S extends Book> Book save(@Param("book") Book book);

	@Override
    @PostFilter("hasPermission(filterObject, 'read') or hasPermission(filterObject, admin)")
	List<Book> findAll();
}
