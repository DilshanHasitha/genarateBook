import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooksOptionDetails } from '../books-option-details.model';
import { BooksOptionDetailsService } from '../service/books-option-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './books-option-details-delete-dialog.component.html',
})
export class BooksOptionDetailsDeleteDialogComponent {
  booksOptionDetails?: IBooksOptionDetails;

  constructor(protected booksOptionDetailsService: BooksOptionDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.booksOptionDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
