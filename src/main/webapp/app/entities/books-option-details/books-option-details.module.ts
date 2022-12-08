import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksOptionDetailsComponent } from './list/books-option-details.component';
import { BooksOptionDetailsDetailComponent } from './detail/books-option-details-detail.component';
import { BooksOptionDetailsUpdateComponent } from './update/books-option-details-update.component';
import { BooksOptionDetailsDeleteDialogComponent } from './delete/books-option-details-delete-dialog.component';
import { BooksOptionDetailsRoutingModule } from './route/books-option-details-routing.module';

@NgModule({
  imports: [SharedModule, BooksOptionDetailsRoutingModule],
  declarations: [
    BooksOptionDetailsComponent,
    BooksOptionDetailsDetailComponent,
    BooksOptionDetailsUpdateComponent,
    BooksOptionDetailsDeleteDialogComponent,
  ],
})
export class BooksOptionDetailsModule {}
