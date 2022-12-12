import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStylesDetails } from '../styles-details.model';
import { StylesDetailsService } from '../service/styles-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './styles-details-delete-dialog.component.html',
})
export class StylesDetailsDeleteDialogComponent {
  stylesDetails?: IStylesDetails;

  constructor(protected stylesDetailsService: StylesDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stylesDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
