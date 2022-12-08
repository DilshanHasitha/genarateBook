import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooksVariables } from '../books-variables.model';
import { BooksVariablesService } from '../service/books-variables.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './books-variables-delete-dialog.component.html',
})
export class BooksVariablesDeleteDialogComponent {
  booksVariables?: IBooksVariables;

  constructor(protected booksVariablesService: BooksVariablesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.booksVariablesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
