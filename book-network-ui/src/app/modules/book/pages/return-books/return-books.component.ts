import { Component, inject, OnInit } from '@angular/core';
import { BookService } from '../../../../services/services';
import { BorrowedBookResponse, PageResponseBorrowedBookResponse } from '../../../../services/models';

@Component({
  selector: 'app-return-books',
  imports: [],
  templateUrl: './return-books.component.html',
  styleUrl: './return-books.component.scss'
})
export class ReturnBooksComponent implements OnInit {

  page = 0;
  size = 5;
  returnedBooks: PageResponseBorrowedBookResponse = {};
  bookService: BookService = inject(BookService);
  message: string = "";
  level: string = "success";

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  findAllReturnedBooks() {
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (res: PageResponseBorrowedBookResponse) => {
        this.returnedBooks = res;
      }
    })
  }

  approveBookReturn(book: BorrowedBookResponse) {

    if (!book.returned) {
      return;
    }

    this.bookService.approveReturnBorrowedBook({
      'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = "success";
        this.message = "Book return approved successfully";
        this.findAllReturnedBooks();
      },
      error: (error) => {
        this.level = "error";
        this.message = error.error.message;
      }
    })

  }

  public get isLastPage(): boolean {
    return this.page == this.returnedBooks.totalPages as number - 1;
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }
  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }
  goToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }
  goToPreviousPage() {
    this.page--;
    this.findAllReturnedBooks();
  }
  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

}
