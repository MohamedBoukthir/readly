import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {BookRoutingModule} from './book-routing.module';
import {MainComponent} from './pages/main/main.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {BookListComponent} from './pages/book-list/book-list.component';
import {BookCardComponent} from './components/book-card/book-card.component';
import {ReadMoreDirective} from "../../directives/read-more.directive";
import {NgOptimizedImageDirective} from "../../directives/ng-optimized-image.directive";


@NgModule({
  declarations: [
    MainComponent,
    NavbarComponent,
    BookListComponent,
    BookCardComponent,
    ReadMoreDirective,
    NgOptimizedImageDirective
  ],
  exports: [
    NavbarComponent
  ],
  imports: [
    CommonModule,
    BookRoutingModule,
  ]
})
export class BookModule {
}
