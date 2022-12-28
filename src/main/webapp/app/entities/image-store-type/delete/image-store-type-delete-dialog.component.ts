import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IImageStoreType } from '../image-store-type.model';
import { ImageStoreTypeService } from '../service/image-store-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './image-store-type-delete-dialog.component.html',
})
export class ImageStoreTypeDeleteDialogComponent {
  imageStoreType?: IImageStoreType;

  constructor(protected imageStoreTypeService: ImageStoreTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imageStoreTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
