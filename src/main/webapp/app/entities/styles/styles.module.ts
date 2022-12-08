import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StylesComponent } from './list/styles.component';
import { StylesDetailComponent } from './detail/styles-detail.component';
import { StylesUpdateComponent } from './update/styles-update.component';
import { StylesDeleteDialogComponent } from './delete/styles-delete-dialog.component';
import { StylesRoutingModule } from './route/styles-routing.module';

@NgModule({
  imports: [SharedModule, StylesRoutingModule],
  declarations: [StylesComponent, StylesDetailComponent, StylesUpdateComponent, StylesDeleteDialogComponent],
})
export class StylesModule {}
