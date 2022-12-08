import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPageSize } from '../page-size.model';
import { PageSizeService } from '../service/page-size.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './page-size-delete-dialog.component.html',
})
export class PageSizeDeleteDialogComponent {
  pageSize?: IPageSize;

  constructor(protected pageSizeService: PageSizeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pageSizeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
