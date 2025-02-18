package com.roque.book.history;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

	@Query("SELECT history FROM BookTransactionHistory history WHERE history.createdBy = :id")
	Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer id);

	@Query("SELECT history FROM BookTransactionHistory history WHERE history.createdBy = :id")
	Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer id);

	@Query("SELECT (COUNT(*) > 0) AS isBorrowed FROM BookTransactionHistory history "
			+ "WHERE history.book.id = :bookId " + "AND history.user.id = :userId "
			+ "AND history.isReturnApproved = false")
	boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

	@Query("SELECT history FROM BookTransactionHistory history " + "WHERE history.user.id = :userId "
			+ "AND history.book.id = :bookId " + "AND history.isReturned = false "
			+ "AND history.isReturnApproved = false")
	Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

	@Query("SELECT history FROM BookTransactionHistory history " + "WHERE history.createdBy = :userId "
			+ "AND history.book.id = :bookId " + "AND history.isReturned = true "
			+ "AND history.isReturnApproved = false")
	Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer id);

}
