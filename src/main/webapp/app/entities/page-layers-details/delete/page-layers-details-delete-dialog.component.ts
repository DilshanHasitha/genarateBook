import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPageLayersDetails } from '../page-layers-details.model';
import { PageLayersDetailsService } from '../service/page-layers-details.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './page-layers-details-delete-dialog.component.html',
})
export class PageLayersDetailsDeleteDialogComponent {
  pageLayersDetails?: IPageLayersDetails;

  constructor(protected pageLayersDetailsService: PageLayersDetailsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageLayersDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
