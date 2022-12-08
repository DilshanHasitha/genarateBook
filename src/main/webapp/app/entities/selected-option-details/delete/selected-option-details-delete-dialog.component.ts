import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISelectedOptionDetails } from '../selected-option-details.model';
import { SelectedOptionDetailsService } from '../service/selected-option-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './selected-option-details-delete-dialog.component.html',
})
export class SelectedOptionDetailsDeleteDialogComponent {
  selectedOptionDetails?: ISelectedOptionDetails;

  constructor(protected selectedOptionDetailsService: SelectedOptionDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.selectedOptionDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
