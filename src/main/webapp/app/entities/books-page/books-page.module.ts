import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksPageComponent } from './list/books-page.component';
import { BooksPageDetailComponent } from './detail/books-page-detail.component';
import { BooksPageUpdateComponent } from './update/books-page-update.component';
import { BooksPageDeleteDialogComponent } from './delete/books-page-delete-dialog.component';
import { BooksPageRoutingModule } from './route/books-page-routing.module';

@NgModule({
  imports: [SharedModule, BooksPageRoutingModule],
  declarations: [BooksPageComponent, BooksPageDetailComponent, BooksPageUpdateComponent, BooksPageDeleteDialogComponent],
})
export class BooksPageModule {}
