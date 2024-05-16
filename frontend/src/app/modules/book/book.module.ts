import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BookRoutingModule } from './book-routing.module';
import { MainComponent } from './pages/main/main.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { BookListComponent } from './pages/book-list/book-list.component';


@NgModule({
  declarations: [
    MainComponent,
    NavbarComponent,
    BookListComponent
  ],
  exports: [
    NavbarComponent
  ],
  imports: [
    CommonModule,
    BookRoutingModule
  ]
})
export class BookModule { }
