import { Component, inject, OnInit } from '@angular/core';
import { BookRequest, BookResponse } from '../../../../services/models';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BookService } from '../../../../services/services';

@Component({
  selector: 'app-manage-book',
  imports: [FormsModule, RouterLink],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss',

})
export class ManageBookComponent implements OnInit {

  private bookService: BookService = inject(BookService);
  private readonly router: Router = inject(Router);
  private activatedRouter: ActivatedRoute = inject(ActivatedRoute);

  errorMessages: Array<string> = [];
  selectedPicture: string | undefined;
  selectedBookCover: any;
  bookRequest: BookRequest = { authorName: '', isbn: '', synopsis: '', title: '' };

  ngOnInit(): void {

    const bookId = this.activatedRouter.snapshot.params['bookId'];

    if (bookId) {
      this.bookService.findBookById({
        'book-id': bookId
      }).subscribe({
        next: (book: BookResponse) => {

          this.bookRequest = {
            id: book.id,
            title: book.title as string,
            authorName: book.authorName as string,
            isbn: book.isbn as string,
            synopsis: book.synopsis as string,
            isShareable: book.shareable
          }

          this.selectedPicture = "data:image/jpg;base64," + book.cover;

        }
      })
    }
  }

  saveBook() {
    this.bookService.save({
      body: this.bookRequest
    }).subscribe({
      next: (bookId: number) => {
        console.log('Book saved with id: ' + bookId);
        // this.bookService.uploadBookCoverPicture({
        //   'book-id': bookId,
        //   body: {
        //     file: this.selectedBookCover
        //   }
        // }).subscribe({
        //   next: () => {
        //     this.router.navigate(['/books/my-books'])
        //   }
        // })
      },
      error: (error) => {
        this.errorMessages = error.error.validationErrors;
      }
    })
  }

  onFileSelected(event: any) {

    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);

    if (this.selectedBookCover) {
      const reader = new FileReader();
      reader.onload = () => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }

  }

}