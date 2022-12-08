import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksRelatedOptionDetailsComponent } from './list/books-related-option-details.component';
import { BooksRelatedOptionDetailsDetailComponent } from './detail/books-related-option-details-detail.component';
import { BooksRelatedOptionDetailsUpdateComponent } from './update/books-related-option-details-update.component';
import { BooksRelatedOptionDetailsDeleteDialogComponent } from './delete/books-related-option-details-delete-dialog.component';
import { BooksRelatedOptionDetailsRoutingModule } from './route/books-related-option-details-routing.module';

@NgModule({
  imports: [SharedModule, BooksRelatedOptionDetailsRoutingModule],
  declarations: [
    BooksRelatedOptionDetailsComponent,
    BooksRelatedOptionDetailsDetailComponent,
    BooksRelatedOptionDetailsUpdateComponent,
    BooksRelatedOptionDetailsDeleteDialogComponent,
  ],
})
export class BooksRelatedOptionDetailsModule {}
