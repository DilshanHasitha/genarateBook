import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILayerDetails } from '../layer-details.model';
import { LayerDetailsService } from '../service/layer-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './layer-details-delete-dialog.component.html',
})
export class LayerDetailsDeleteDialogComponent {
  layerDetails?: ILayerDetails;

  constructor(protected layerDetailsService: LayerDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.layerDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
