import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AvatarCharactorComponent } from './list/avatar-charactor.component';
import { AvatarCharactorDetailComponent } from './detail/avatar-charactor-detail.component';
import { AvatarCharactorUpdateComponent } from './update/avatar-charactor-update.component';
import { AvatarCharactorDeleteDialogComponent } from './delete/avatar-charactor-delete-dialog.component';
import { AvatarCharactorRoutingModule } from './route/avatar-charactor-routing.module';

@NgModule({
  imports: [SharedModule, AvatarCharactorRoutingModule],
  declarations: [
    AvatarCharactorComponent,
    AvatarCharactorDetailComponent,
    AvatarCharactorUpdateComponent,
    AvatarCharactorDeleteDialogComponent,
  ],
})
export class AvatarCharactorModule {}
