import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/services';
import { Router } from '@angular/router';
import { BookResponse, PageResponseBookResponse } from '../../services/models';
import { BookCardComponent } from '../../modules/book/components/book-card/book-card.component';

@Component({
	selector: 'app-book-list',
	templateUrl: './book-list.component.html',
	styleUrl: './book-list.component.scss',
	imports: [BookCardComponent]
})
export class BookListComponent implements OnInit {

	message: string = '';
	level: string = 'success';
	public page: number = 0;
	public size: number = 2;
	public bookResponse: PageResponseBookResponse = {};

	constructor(
		private bookService: BookService,
		private router: Router,
	) { }

	borrowBook(book: BookResponse): void {

		this.level = 'success';
		this.message = "";

		this.bookService.borrowBook({
			'book-id': book.id as number
		}).subscribe({
			next: () => {
				this.level = 'success';
				this.message = 'Book successfully added to yout list';
			},
			error: (err) => {
				console.log(err);
				this.level = 'error';
				this.message = err.error.error;
			}
		}
		)
	}

	ngOnInit(): void {
		this.findAllBooks();
	}

	private findAllBooks(): void {
		this.bookService.findAllBooks({
			page: this.page,
			size: this.size
		}).subscribe({
			next: (books: PageResponseBookResponse): void => {
				this.bookResponse = books;
			}
		})
	}

	public get isLastPage(): boolean {
		return this.page == this.bookResponse.totalPages as number - 1;
	}

	goToLastPage() {
		this.page = this.bookResponse.totalPages as number - 1;
		this.findAllBooks();
	}
	goToNextPage() {
		this.page++;
		this.findAllBooks();
	}
	goToPage(page: number) {
		this.page = page;
		this.findAllBooks();
	}
	goToPreviousPage() {
		this.page--;
		this.findAllBooks();
	}
	goToFirstPage() {
		this.page = 0;
		this.findAllBooks();
	}

}
