import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OptionTypeComponent } from './list/option-type.component';
import { OptionTypeDetailComponent } from './detail/option-type-detail.component';
import { OptionTypeUpdateComponent } from './update/option-type-update.component';
import { OptionTypeDeleteDialogComponent } from './delete/option-type-delete-dialog.component';
import { OptionTypeRoutingModule } from './route/option-type-routing.module';

@NgModule({
  imports: [SharedModule, OptionTypeRoutingModule],
  declarations: [OptionTypeComponent, OptionTypeDetailComponent, OptionTypeUpdateComponent, OptionTypeDeleteDialogComponent],
})
export class OptionTypeModule {}
