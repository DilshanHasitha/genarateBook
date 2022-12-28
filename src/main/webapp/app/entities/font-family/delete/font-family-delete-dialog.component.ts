import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFontFamily } from '../font-family.model';
import { FontFamilyService } from '../service/font-family.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './font-family-delete-dialog.component.html',
})
export class FontFamilyDeleteDialogComponent {
  fontFamily?: IFontFamily;

  constructor(protected fontFamilyService: FontFamilyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fontFamilyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
