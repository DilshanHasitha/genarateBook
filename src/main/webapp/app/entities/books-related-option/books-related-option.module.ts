import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksRelatedOptionComponent } from './list/books-related-option.component';
import { BooksRelatedOptionDetailComponent } from './detail/books-related-option-detail.component';
import { BooksRelatedOptionUpdateComponent } from './update/books-related-option-update.component';
import { BooksRelatedOptionDeleteDialogComponent } from './delete/books-related-option-delete-dialog.component';
import { BooksRelatedOptionRoutingModule } from './route/books-related-option-routing.module';

@NgModule({
  imports: [SharedModule, BooksRelatedOptionRoutingModule],
  declarations: [
    BooksRelatedOptionComponent,
    BooksRelatedOptionDetailComponent,
    BooksRelatedOptionUpdateComponent,
    BooksRelatedOptionDeleteDialogComponent,
  ],
})
export class BooksRelatedOptionModule {}
