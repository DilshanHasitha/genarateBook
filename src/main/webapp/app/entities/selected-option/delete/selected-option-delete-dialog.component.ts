import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISelectedOption } from '../selected-option.model';
import { SelectedOptionService } from '../service/selected-option.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './selected-option-delete-dialog.component.html',
})
export class SelectedOptionDeleteDialogComponent {
  selectedOption?: ISelectedOption;

  constructor(protected selectedOptionService: SelectedOptionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.selectedOptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
