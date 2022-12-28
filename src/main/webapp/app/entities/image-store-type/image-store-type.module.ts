import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImageStoreTypeComponent } from './list/image-store-type.component';
import { ImageStoreTypeDetailComponent } from './detail/image-store-type-detail.component';
import { ImageStoreTypeUpdateComponent } from './update/image-store-type-update.component';
import { ImageStoreTypeDeleteDialogComponent } from './delete/image-store-type-delete-dialog.component';
import { ImageStoreTypeRoutingModule } from './route/image-store-type-routing.module';

@NgModule({
  imports: [SharedModule, ImageStoreTypeRoutingModule],
  declarations: [
    ImageStoreTypeComponent,
    ImageStoreTypeDetailComponent,
    ImageStoreTypeUpdateComponent,
    ImageStoreTypeDeleteDialogComponent,
  ],
})
export class ImageStoreTypeModule {}
