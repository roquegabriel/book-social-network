package com.roque.book.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

	@Query("""
			SELECT b
			FROM Book b
			JOIN FETCH b.owner o
			WHERE b.isArchived = false
			AND b.isShareable = true
			AND b.createdBy != :userId
			""")
	Page<Book> findAllDisplayableBooks(Pageable pageable, int userId);

}
