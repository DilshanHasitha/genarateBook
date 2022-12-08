import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooksRelatedOption } from '../books-related-option.model';
import { BooksRelatedOptionService } from '../service/books-related-option.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './books-related-option-delete-dialog.component.html',
})
export class BooksRelatedOptionDeleteDialogComponent {
  booksRelatedOption?: IBooksRelatedOption;

  constructor(protected booksRelatedOptionService: BooksRelatedOptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.booksRelatedOptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
