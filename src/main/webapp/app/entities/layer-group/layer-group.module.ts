import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LayerGroupComponent } from './list/layer-group.component';
import { LayerGroupDetailComponent } from './detail/layer-group-detail.component';
import { LayerGroupUpdateComponent } from './update/layer-group-update.component';
import { LayerGroupDeleteDialogComponent } from './delete/layer-group-delete-dialog.component';
import { LayerGroupRoutingModule } from './route/layer-group-routing.module';

@NgModule({
  imports: [SharedModule, LayerGroupRoutingModule],
  declarations: [LayerGroupComponent, LayerGroupDetailComponent, LayerGroupUpdateComponent, LayerGroupDeleteDialogComponent],
})
export class LayerGroupModule {}
