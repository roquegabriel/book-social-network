import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../services/models';
import { RatingComponent } from "../rating/rating.component";
import { CommonModule } from '@angular/common';


@Component({
	selector: 'app-book-card',
	templateUrl: './book-card.component.html',
	styleUrl: './book-card.component.scss',
	imports: [RatingComponent, CommonModule]
})
export class BookCardComponent {

	private _book: BookResponse = {}
	private _bookCover: string | undefined
	private _manage: boolean = false

	public get manage(): boolean {
		return this._manage
	}

	@Input()
	public set manage(manage: boolean) {
		this._manage = manage;
	}

	public get book() {
		return this._book
	}

	@Input()
	public set book(book: BookResponse) {
		this._book = book
	}

	public get bookCover() {
		if (this._book.cover) {
			return "data:image/png;base64," + this._book.cover
		}
		return '/default.jpg';
	}

	public set bookCover(bookCover: string | undefined) {
		this._bookCover = bookCover
	}

	@Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
	@Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
	@Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
	@Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
	@Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
	@Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

	onArchive() {
		this.archive.emit(this._book);
	}

	onShare() {
		this.share.emit(this._book);
	}

	onEdit() {
		this.edit.emit(this._book);
	}

	onAddToWaitingList() {
		this.addToWaitingList.emit(this._book);
	}

	onBorrow() {
		this.borrow.emit(this._book);
	}

	onShowDetails() {
		this.details.emit(this._book);
	}

}