import { Component, inject, OnInit } from '@angular/core';
import { BorrowedBookResponse, FeedbackRequest, PageResponseBorrowedBookResponse } from '../../services/models';
import { BookService, FeedbackService } from '../../services/services';
import { FormsModule } from '@angular/forms';
import { RatingComponent } from "../../modules/book/components/rating/rating.component";
import { RouterLink } from '@angular/router';


@Component({
  selector: 'app-borrowed-book-list',
  imports: [FormsModule, RatingComponent],
  templateUrl: './borrowed-book-list.component.html',
  styleUrl: './borrowed-book-list.component.scss'
})
export class BorrowedBookListComponent implements OnInit {

  page = 0;
  size = 5;
  borrowedBook: PageResponseBorrowedBookResponse = {};
  bookService: BookService = inject(BookService);
  feedbackService: FeedbackService = inject(FeedbackService);
  selectedBook: BorrowedBookResponse | undefined = undefined;
  feedbackRequest: FeedbackRequest = { 'bookId': 0, 'comment': '', 'note': 0 };

  ngOnInit(): void {
    this.findAllBorrowedBooks();
  }

  findAllBorrowedBooks() {
    this.bookService.findAllBorrowedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (res: PageResponseBorrowedBookResponse) => {
        this.borrowedBook = res;
      }
    })
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }

  returnBook(withFeedback: boolean) {
    this.bookService.returnBorrowedBook({
      'book-id': this.selectedBook?.id as number,
    }).subscribe({
      next: () => {
        if (withFeedback) {
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    })
  }

  giveFeedback() {
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: () => {
        this.findAllBorrowedBooks();        
      }
    })
  }

  public get isLastPage(): boolean {
    return this.page == this.borrowedBook.totalPages as number - 1;
  }

  goToLastPage() {
    this.page = this.borrowedBook.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }
  goToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllBorrowedBooks();
  }
  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

}
