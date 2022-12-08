import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBooksAttributes } from '../books-attributes.model';
import { BooksAttributesService } from '../service/books-attributes.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './books-attributes-delete-dialog.component.html',
})
export class BooksAttributesDeleteDialogComponent {
  booksAttributes?: IBooksAttributes;

  constructor(protected booksAttributesService: BooksAttributesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.booksAttributesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
