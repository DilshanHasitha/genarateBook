import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StylesDetailsComponent } from './list/styles-details.component';
import { StylesDetailsDetailComponent } from './detail/styles-details-detail.component';
import { StylesDetailsUpdateComponent } from './update/styles-details-update.component';
import { StylesDetailsDeleteDialogComponent } from './delete/styles-details-delete-dialog.component';
import { StylesDetailsRoutingModule } from './route/styles-details-routing.module';

@NgModule({
  imports: [SharedModule, StylesDetailsRoutingModule],
  declarations: [StylesDetailsComponent, StylesDetailsDetailComponent, StylesDetailsUpdateComponent, StylesDetailsDeleteDialogComponent],
})
export class StylesDetailsModule {}
