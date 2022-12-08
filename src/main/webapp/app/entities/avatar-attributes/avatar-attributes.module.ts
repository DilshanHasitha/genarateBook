import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AvatarAttributesComponent } from './list/avatar-attributes.component';
import { AvatarAttributesDetailComponent } from './detail/avatar-attributes-detail.component';
import { AvatarAttributesUpdateComponent } from './update/avatar-attributes-update.component';
import { AvatarAttributesDeleteDialogComponent } from './delete/avatar-attributes-delete-dialog.component';
import { AvatarAttributesRoutingModule } from './route/avatar-attributes-routing.module';

@NgModule({
  imports: [SharedModule, AvatarAttributesRoutingModule],
  declarations: [
    AvatarAttributesComponent,
    AvatarAttributesDetailComponent,
    AvatarAttributesUpdateComponent,
    AvatarAttributesDeleteDialogComponent,
  ],
})
export class AvatarAttributesModule {}
