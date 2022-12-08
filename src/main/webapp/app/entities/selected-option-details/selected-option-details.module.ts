import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SelectedOptionDetailsComponent } from './list/selected-option-details.component';
import { SelectedOptionDetailsDetailComponent } from './detail/selected-option-details-detail.component';
import { SelectedOptionDetailsUpdateComponent } from './update/selected-option-details-update.component';
import { SelectedOptionDetailsDeleteDialogComponent } from './delete/selected-option-details-delete-dialog.component';
import { SelectedOptionDetailsRoutingModule } from './route/selected-option-details-routing.module';

@NgModule({
  imports: [SharedModule, SelectedOptionDetailsRoutingModule],
  declarations: [
    SelectedOptionDetailsComponent,
    SelectedOptionDetailsDetailComponent,
    SelectedOptionDetailsUpdateComponent,
    SelectedOptionDetailsDeleteDialogComponent,
  ],
})
export class SelectedOptionDetailsModule {}
