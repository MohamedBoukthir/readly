import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {BookRoutingModule} from './book-routing.module';
import {MainComponent} from './pages/main/main.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {BookListComponent} from './pages/book-list/book-list.component';
import {BookCardComponent} from './components/book-card/book-card.component';
import {ReadMoreDirective} from "../../directives/read-more.directive";
import {NgOptimizedImageDirective} from "../../directives/ng-optimized-image.directive";
import { RateComponent } from './components/rate/rate.component';
import { MyBooksComponent } from './pages/my-books/my-books.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import {FormsModule} from "@angular/forms";
import { BorrowedBookListComponent } from './pages/borrowed-book-list/borrowed-book-list.component';


@NgModule({
  declarations: [
    MainComponent,
    NavbarComponent,
    BookListComponent,
    BookCardComponent,
    ReadMoreDirective,
    NgOptimizedImageDirective,
    RateComponent,
    MyBooksComponent,
    ManageBookComponent,
    BorrowedBookListComponent,
  ],
  exports: [
    NavbarComponent
  ],
  imports: [
    CommonModule,
    BookRoutingModule,
    FormsModule,
  ]
})
export class BookModule {
}
