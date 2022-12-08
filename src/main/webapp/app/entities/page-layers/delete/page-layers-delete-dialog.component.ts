import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPageLayers } from '../page-layers.model';
import { PageLayersService } from '../service/page-layers.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './page-layers-delete-dialog.component.html',
})
export class PageLayersDeleteDialogComponent {
  pageLayers?: IPageLayers;

  constructor(protected pageLayersService: PageLayersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageLayersService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
