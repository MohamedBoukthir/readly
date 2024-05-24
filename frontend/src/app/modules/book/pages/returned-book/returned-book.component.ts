import {Component, OnInit} from '@angular/core';
import {PageResponseBorrowedBookResponse} from "../../../../services/models/page-response-borrowed-book-response";
import {BorrowedBookResponse} from "../../../../services/models/borrowed-book-response";
import {BookService} from "../../../../services/services/book.service";

@Component({
  selector: 'app-returned-book',
  templateUrl: './returned-book.component.html',
  styleUrl: './returned-book.component.css'
})
export class ReturnedBookComponent implements OnInit{

  returnedBooks: PageResponseBorrowedBookResponse = {};
  page = 0;
  size = 10;
  message: string = '';
  level = 'Success';


  constructor(
    private bookService: BookService,
  ) { }

  ngOnInit(): void {
    this.findAllReturnedBooks();
  }

  private findAllReturnedBooks() {
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size,
    }).subscribe({
      next: (response) => {
        this.returnedBooks = response;
      },
      error: (error) => {
        console.error('There was an error!', error);
      }
    })
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page ++;
    this.findAllReturnedBooks();
  }

  get isLastPage() {
    return this.page === this.returnedBooks.totalPages as number - 1;
  }

  approveBookReturn(book: BorrowedBookResponse) {
    if (!book.returned) {
      this.level = 'Error';
      this.message = 'Book has not been returned yet';
      return;
    }
    this.bookService.approveReturnBorrowBook({
      'book-id': book.id as number,
    }).subscribe({
     next: () => {
       this.level = 'Success';
       this.message = 'Book return approved successfully';
       this.findAllReturnedBooks();
     }
    });
  }

  getAlertClass() {
    return this.level === 'Success' ? 'flex bg-green-100 rounded-lg p-4 mb-4 text-sm text-green-700' : 'flex bg-red-100 rounded-lg p-4 mb-4 text-sm text-red-700';
  }
}
