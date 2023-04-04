import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrgProperty } from '../org-property.model';
import { OrgPropertyService } from '../service/org-property.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './org-property-delete-dialog.component.html',
})
export class OrgPropertyDeleteDialogComponent {
  orgProperty?: IOrgProperty;

  constructor(protected orgPropertyService: OrgPropertyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orgPropertyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
