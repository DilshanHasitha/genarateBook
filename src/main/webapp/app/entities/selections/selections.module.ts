import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SelectionsComponent } from './list/selections.component';
import { SelectionsDetailComponent } from './detail/selections-detail.component';
import { SelectionsUpdateComponent } from './update/selections-update.component';
import { SelectionsDeleteDialogComponent } from './delete/selections-delete-dialog.component';
import { SelectionsRoutingModule } from './route/selections-routing.module';

@NgModule({
  imports: [SharedModule, SelectionsRoutingModule],
  declarations: [SelectionsComponent, SelectionsDetailComponent, SelectionsUpdateComponent, SelectionsDeleteDialogComponent],
})
export class SelectionsModule {}
