package com.roque.book.book;

import static com.roque.book.book.BookSpecification.withOwnerId;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.roque.book.common.PageResponse;
import com.roque.book.exception.OperationNotPermittedException;
import com.roque.book.file.FileStorageService;
import com.roque.book.history.BookTransactionHistory;
import com.roque.book.history.BookTransactionHistoryRepository;
import com.roque.book.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookMapper bookMapper;
	private final BookRepository bookRepository;
	private final BookTransactionHistoryRepository transactionHistoryRepository;
	private final FileStorageService fileStorageService;

	public Integer save(BookRequest request, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();
		Book book = bookMapper.toBook(request);
		book.setOwner(user);
		return bookRepository.save(book).getId();
	}

	public BookResponse findById(Integer id) {
		return bookRepository.findById(id).map(bookMapper::toBookResponse)
				.orElseThrow(() -> new EntityNotFoundException("Not book found with the id " + id));
	}

	public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
		List<BookResponse> bookReponse = books.stream().map(bookMapper::toBookResponse).toList();
		return new PageResponse<BookResponse>(bookReponse, books.getNumber(), books.getSize(), books.getTotalElements(),
				books.getTotalPages(), books.isFirst(), books.isLast());
	}

	public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
		List<BookResponse> bookReponse = books.stream().map(bookMapper::toBookResponse).toList();
		return new PageResponse<BookResponse>(bookReponse, books.getNumber(), books.getSize(), books.getTotalElements(),
				books.getTotalPages(), books.isFirst(), books.isLast());
	}

	public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable,
				user.getId());
		List<BorrowedBookResponse> borrowedBookResponse = allBorrowedBooks.stream()
				.map(bookMapper::toBorrowedBookResponse).toList();
		return new PageResponse<BorrowedBookResponse>(borrowedBookResponse, allBorrowedBooks.getNumber(),
				allBorrowedBooks.getSize(), allBorrowedBooks.getTotalElements(), allBorrowedBooks.getTotalPages(),
				allBorrowedBooks.isFirst(), allBorrowedBooks.isLast());
	}

	public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
		Page<BookTransactionHistory> allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable,
				user.getId());
		List<BorrowedBookResponse> borrowedBookResponse = allBorrowedBooks.stream()
				.map(bookMapper::toBorrowedBookResponse).toList();
		return new PageResponse<BorrowedBookResponse>(borrowedBookResponse, allBorrowedBooks.getNumber(),
				allBorrowedBooks.getSize(), allBorrowedBooks.getTotalElements(), allBorrowedBooks.getTotalPages(),
				allBorrowedBooks.isFirst(), allBorrowedBooks.isLast());
	}

	public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("No book found with the id " + bookId));
		User user = (User) connectedUser.getPrincipal();
		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You cannot update others books shareable status");
		}
		book.setShareable(!book.isShareable());
		bookRepository.save(book);
		return bookId;
	}

	public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("No book found with the id " + bookId));
		User user = (User) connectedUser.getPrincipal();
		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You cannot update others books shareable status");
		}
		book.setArchived(!book.isArchived());
		bookRepository.save(book);
		return bookId;
	}

	public Integer borrowBook(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("No book found with the id " + bookId));
		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException(
					"The requested book cannot be borrowed since it is archived or not shareable");
		}
		User user = (User) connectedUser.getPrincipal();
		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("You cannot borrow your own book");
		}

		final boolean isAlreadyBorrowed = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
		if (isAlreadyBorrowed) {
			throw new OperationNotPermittedException("The requested book is already borrowed");
		}

		BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder().user(user).book(book)
				.isReturned(false).isReturnApproved(false).build();

		return transactionHistoryRepository.save(bookTransactionHistory).getId();
	}

	public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("No book found with the id " + bookId));
		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException(
					"The requested book cannot be borrowed since it is archived or not shareable");
		}
		User user = (User) connectedUser.getPrincipal();
		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("You cannot borrow or return your own book");
		}

		BookTransactionHistory bookTransactionHistory = transactionHistoryRepository
				.findByBookIdAndUserId(bookId, user.getId())
				.orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book"));

		bookTransactionHistory.setReturned(true);
		return transactionHistoryRepository.save(bookTransactionHistory).getId();
	}

	public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("No book found with the id " + bookId));
		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException(
					"The requested book cannot be borrowed since it is archived or not shareable");
		}
		User user = (User) connectedUser.getPrincipal();
		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("You cannot borrow or return your own book");
		}
		BookTransactionHistory bookTransactionHistory = transactionHistoryRepository
				.findByBookIdAndOwnerId(bookId, user.getId()).orElseThrow(() -> new OperationNotPermittedException(
						"The book is not returned yet. You cannot approve its return"));
		return transactionHistoryRepository.save(bookTransactionHistory).getId();
	}

	public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("No book found with the id " + bookId));
		User user = (User) connectedUser.getPrincipal();
		var bookCover = fileStorageService.saveFile(file, user.getId());
		book.setBookCover(bookCover);
		bookRepository.save(book);

	}

}
