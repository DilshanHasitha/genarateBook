import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAvatarAttributes } from '../avatar-attributes.model';
import { AvatarAttributesService } from '../service/avatar-attributes.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './avatar-attributes-delete-dialog.component.html',
})
export class AvatarAttributesDeleteDialogComponent {
  avatarAttributes?: IAvatarAttributes;

  constructor(protected avatarAttributesService: AvatarAttributesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.avatarAttributesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
