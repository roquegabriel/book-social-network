package com.roque.book.book;

import java.util.List;

import com.roque.book.common.BaseEntity;
import com.roque.book.feedback.Feedback;
import com.roque.book.history.BookTransactionHistory;
import com.roque.book.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

	private String title;
	private String authorName;
	private String isbn;
	private String synopsis;
	private String bookCover;
	private boolean isArchived;
	private boolean isShareable;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	@OneToMany(mappedBy = "book")
	private List<Feedback> feedbacks;

	@OneToMany(mappedBy = "book")
	private List<BookTransactionHistory> histories;

	@Transient
	public double getRate() {

		if (feedbacks == null || feedbacks.isEmpty()) {
			return 0.0;
		}

		var rate = this.feedbacks.stream().mapToDouble(Feedback::getNote).average().orElse(0.0);

		double roundedRate = Math.round(rate * 10.0) / 10.0;

		return roundedRate;
	}
}
