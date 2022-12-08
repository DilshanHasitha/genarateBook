import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriceRelatedOption } from '../price-related-option.model';
import { PriceRelatedOptionService } from '../service/price-related-option.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './price-related-option-delete-dialog.component.html',
})
export class PriceRelatedOptionDeleteDialogComponent {
  priceRelatedOption?: IPriceRelatedOption;

  constructor(protected priceRelatedOptionService: PriceRelatedOptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.priceRelatedOptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
