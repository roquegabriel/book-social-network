package com.roque.book.book;

import org.springframework.stereotype.Service;

import com.roque.book.file.FileUtils;
import com.roque.book.history.BookTransactionHistory;

@Service
public class BookMapper {

	public Book toBook(BookRequest request) {
		return Book.builder()
				.id(request.id())
				.title(request.title())
				.authorName(request.authorName())
				.synopsis(request.synopsis())
				.isArchived(false)
				.isShareable(request.isShareable())
				.isbn(request.isbn())
				.build();
	}

	public BookResponse toBookResponse(Book book) {
		return BookResponse.builder()
				.id(book.getId())
				.title(book.getTitle())
				.authorName(book.getAuthorName())
				.isArchived(book.isArchived())
				.isbn(book.getIsbn())
				.synopsis(book.getSynopsis())
				.isShareable(book.isShareable())
				.rate(book.getRate())
				.owner(book.getOwner().fullName())
				.cover(FileUtils.readFileFromLocation(book.getBookCover()))
				.build();
	}

	public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
		return BorrowedBookResponse.builder().id(history.getBook().getId()).title(history.getBook().getTitle())
				.authorName(history.getBook().getAuthorName()).isbn(history.getBook().getIsbn())
				.rate(history.getBook().getRate()).isReturned(history.isReturned())
				.isReturnApproved(history.isReturnApproved()).build();
	}
	
}
