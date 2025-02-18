package com.roque.book.feedback;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.roque.book.book.Book;
import com.roque.book.book.BookRepository;
import com.roque.book.common.PageResponse;
import com.roque.book.exception.OperationNotPermittedException;
import com.roque.book.user.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackService {
	private final BookRepository bookRepository;
	private final FeedbackMapper feedbackMapper;
	private final FeedbackRepository feedbackRepository;

	public Integer save(FeedbackRequest request, Authentication connectedUser) {
		Book book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new EntityNotFoundException("No book found with the ID " + request.bookId()));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException(
					"You cannot give a feedback for an archived and not shareable book");
		}
		User user = (User) connectedUser.getPrincipal();
		if (Objects.equals(user.getId(), book.getOwner().getId())) {
			throw new OperationNotPermittedException("You cannot give a feedback to your own book");
		}

		Feedback feedback = feedbackMapper.toFeedback(request);
		return feedbackRepository.save(feedback).getId();
	}

	public PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size,
			Authentication connectedUser) {
		Pageable pageable = PageRequest.of(page, size);
		User user = (User) connectedUser.getPrincipal();
		Page<Feedback> feedback = feedbackRepository.findAllByBookId(bookId, pageable);
		List<FeedbackResponse> feedbackResponses = feedback.stream()
				.map(f -> feedbackMapper.toFeedbackResponse(f, user.getId())).collect(Collectors.toList());
		return new PageResponse<FeedbackResponse>(feedbackResponses, feedback.getNumber(), feedback.getSize(),
				feedback.getTotalElements(), feedback.getTotalPages(), feedback.isFirst(), feedback.isLast());
	}
}