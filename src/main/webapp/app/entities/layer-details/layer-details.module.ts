import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LayerDetailsComponent } from './list/layer-details.component';
import { LayerDetailsDetailComponent } from './detail/layer-details-detail.component';
import { LayerDetailsUpdateComponent } from './update/layer-details-update.component';
import { LayerDetailsDeleteDialogComponent } from './delete/layer-details-delete-dialog.component';
import { LayerDetailsRoutingModule } from './route/layer-details-routing.module';

@NgModule({
  imports: [SharedModule, LayerDetailsRoutingModule],
  declarations: [LayerDetailsComponent, LayerDetailsDetailComponent, LayerDetailsUpdateComponent, LayerDetailsDeleteDialogComponent],
})
export class LayerDetailsModule {}
