import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BooksVariablesComponent } from './list/books-variables.component';
import { BooksVariablesDetailComponent } from './detail/books-variables-detail.component';
import { BooksVariablesUpdateComponent } from './update/books-variables-update.component';
import { BooksVariablesDeleteDialogComponent } from './delete/books-variables-delete-dialog.component';
import { BooksVariablesRoutingModule } from './route/books-variables-routing.module';

@NgModule({
  imports: [SharedModule, BooksVariablesRoutingModule],
  declarations: [
    BooksVariablesComponent,
    BooksVariablesDetailComponent,
    BooksVariablesUpdateComponent,
    BooksVariablesDeleteDialogComponent,
  ],
})
export class BooksVariablesModule {}
