import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriceRelatedOptionDetails } from '../price-related-option-details.model';
import { PriceRelatedOptionDetailsService } from '../service/price-related-option-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './price-related-option-details-delete-dialog.component.html',
})
export class PriceRelatedOptionDetailsDeleteDialogComponent {
  priceRelatedOptionDetails?: IPriceRelatedOptionDetails;

  constructor(protected priceRelatedOptionDetailsService: PriceRelatedOptionDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.priceRelatedOptionDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
