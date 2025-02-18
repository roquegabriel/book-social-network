package com.roque.book.book;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.roque.book.auth.ContentDto;
import com.roque.book.common.PageResponse;
import com.roque.book.user.User;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "book")
public class BookController {

	private final BookService service;

	@PostMapping
	public ResponseEntity<Integer> save(@Valid @RequestBody BookRequest request, Authentication connectedUser) {
		return ResponseEntity.ok(service.save(request, connectedUser));
	}

	@GetMapping("{book-id}")
	public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@GetMapping
	public ResponseEntity<PageResponse<BookResponse>> findAllBooks(
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.findAllBooks(page, size, connectedUser));
	}

	@GetMapping("/owner")
	public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
			@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.findAllBooksByOwner(page, size, connectedUser));
	}

	@GetMapping("/borrowed")
	public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
			@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, connectedUser));
	}

	@GetMapping("/returned")
	public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
			@RequestParam(defaultValue = "0", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.findAllReturnedBooks(page, size, connectedUser));
	}

	@PatchMapping("/shareable/{book-id}")
	public ResponseEntity<Integer> updateShareableStatus(@PathVariable("book-id") Integer bookId,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.updateShareableStatus(bookId, connectedUser));
	}

	@PatchMapping("/archived/{book-id}")
	public ResponseEntity<Integer> updateArchivedStatus(@PathVariable("book-id") Integer bookId,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.updateArchivedStatus(bookId, connectedUser));
	}

	@PostMapping("/borrow/{book-id}")
	public ResponseEntity<Integer> borrowBook(@PathVariable("book-id") Integer bookId, Authentication connectedUser) {

		return ResponseEntity.ok(service.borrowBook(bookId, connectedUser));
	}

	@PatchMapping("/borrow/return/{book-id}")
	public ResponseEntity<Integer> returnBorrowedBook(@PathVariable("book-id") Integer bookId,
			Authentication connectedUser) {

		return ResponseEntity.ok(service.returnBorrowedBook(bookId, connectedUser));
	}

	@PatchMapping("/borrow/return/approve/{book-id}")
	public ResponseEntity<Integer> approveReturnBorrowedBook(@PathVariable("book-id") Integer bookId,
			Authentication connectedUser) {
		return ResponseEntity.ok(service.approveReturnBorrowedBook(bookId, connectedUser));
	}

	@PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
	public ResponseEntity<?> uploadBookCoverPicture(@PathVariable("book-id") Integer bookId,
			@Parameter() @RequestPart("file") MultipartFile file, Authentication connectedUser) {
		service.uploadBookCoverPicture(file, connectedUser, bookId);
		return ResponseEntity.accepted().build();
	}

	@GetMapping("/test-request")
	public ResponseEntity<ContentDto> testPostRequest() {
		Authentication connectedUser = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) connectedUser.getPrincipal();		
		return ResponseEntity.ok(new ContentDto("Hello world from backend book controller", user.getBooks().get(0).getTitle()));
	}
}
