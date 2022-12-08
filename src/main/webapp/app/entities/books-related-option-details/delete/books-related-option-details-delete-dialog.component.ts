import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooksRelatedOptionDetails } from '../books-related-option-details.model';
import { BooksRelatedOptionDetailsService } from '../service/books-related-option-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './books-related-option-details-delete-dialog.component.html',
})
export class BooksRelatedOptionDetailsDeleteDialogComponent {
  booksRelatedOptionDetails?: IBooksRelatedOptionDetails;

  constructor(protected booksRelatedOptionDetailsService: BooksRelatedOptionDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.booksRelatedOptionDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
