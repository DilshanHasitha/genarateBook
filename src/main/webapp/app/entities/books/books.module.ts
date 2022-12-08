import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksComponent } from './list/books.component';
import { BooksDetailComponent } from './detail/books-detail.component';
import { BooksUpdateComponent } from './update/books-update.component';
import { BooksDeleteDialogComponent } from './delete/books-delete-dialog.component';
import { BooksRoutingModule } from './route/books-routing.module';

@NgModule({
  imports: [SharedModule, BooksRoutingModule],
  declarations: [BooksComponent, BooksDetailComponent, BooksUpdateComponent, BooksDeleteDialogComponent],
})
export class BooksModule {}
