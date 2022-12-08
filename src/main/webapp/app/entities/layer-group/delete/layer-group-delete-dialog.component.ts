import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILayerGroup } from '../layer-group.model';
import { LayerGroupService } from '../service/layer-group.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './layer-group-delete-dialog.component.html',
})
export class LayerGroupDeleteDialogComponent {
  layerGroup?: ILayerGroup;

  constructor(protected layerGroupService: LayerGroupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.layerGroupService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
