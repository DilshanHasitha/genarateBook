import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAvatarCharactor } from '../avatar-charactor.model';
import { AvatarCharactorService } from '../service/avatar-charactor.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './avatar-charactor-delete-dialog.component.html',
})
export class AvatarCharactorDeleteDialogComponent {
  avatarCharactor?: IAvatarCharactor;

  constructor(protected avatarCharactorService: AvatarCharactorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.avatarCharactorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
