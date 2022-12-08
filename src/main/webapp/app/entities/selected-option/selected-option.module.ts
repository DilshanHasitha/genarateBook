import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SelectedOptionComponent } from './list/selected-option.component';
import { SelectedOptionDetailComponent } from './detail/selected-option-detail.component';
import { SelectedOptionUpdateComponent } from './update/selected-option-update.component';
import { SelectedOptionDeleteDialogComponent } from './delete/selected-option-delete-dialog.component';
import { SelectedOptionRoutingModule } from './route/selected-option-routing.module';

@NgModule({
  imports: [SharedModule, SelectedOptionRoutingModule],
  declarations: [
    SelectedOptionComponent,
    SelectedOptionDetailComponent,
    SelectedOptionUpdateComponent,
    SelectedOptionDeleteDialogComponent,
  ],
})
export class SelectedOptionModule {}
