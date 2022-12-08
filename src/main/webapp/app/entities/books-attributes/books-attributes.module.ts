import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksAttributesComponent } from './list/books-attributes.component';
import { BooksAttributesDetailComponent } from './detail/books-attributes-detail.component';
import { BooksAttributesUpdateComponent } from './update/books-attributes-update.component';
import { BooksAttributesDeleteDialogComponent } from './delete/books-attributes-delete-dialog.component';
import { BooksAttributesRoutingModule } from './route/books-attributes-routing.module';

@NgModule({
  imports: [SharedModule, BooksAttributesRoutingModule],
  declarations: [
    BooksAttributesComponent,
    BooksAttributesDetailComponent,
    BooksAttributesUpdateComponent,
    BooksAttributesDeleteDialogComponent,
  ],
})
export class BooksAttributesModule {}
