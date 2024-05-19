import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";
import {BookResponse} from "../../../../services/models/book-response";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.css'
})
export class BookListComponent implements OnInit {

  bookResponse: PageResponseBookResponse = {};
  size = 10;
  page = 0;
  message: string = '';
  level = 'Success';

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();
  }


  private findAllBooks() {
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next: (books) => {
        console.log(books);
        this.bookResponse = books;
      }
    })
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page ++;
    this.findAllBooks();
  }

  get isLastPage() {
    return this.page === this.bookResponse.totalPages as number - 1;
  }


  borrowBook(book: BookResponse) {
    this.message = '';
    this.level = 'Success';
    this.bookService.borrowBook({
    'book-id': book.id as number
    }).subscribe({
      next: () => {
        this.level = 'Success';
        this.message = 'Book borrowed successfully';
      },
      error: (error) => {
        console.log(error);
        this.level = 'Error';
        this.message = error.error.error;
      }
    });
  }

  getAlertClass() {
    return this.level === 'Success' ? 'flex bg-green-100 rounded-lg p-4 mb-4 text-sm text-green-700' : 'flex bg-red-100 rounded-lg p-4 mb-4 text-sm text-red-700';
  }
}
