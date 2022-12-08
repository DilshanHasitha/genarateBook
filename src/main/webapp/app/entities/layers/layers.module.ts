import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LayersComponent } from './list/layers.component';
import { LayersDetailComponent } from './detail/layers-detail.component';
import { LayersUpdateComponent } from './update/layers-update.component';
import { LayersDeleteDialogComponent } from './delete/layers-delete-dialog.component';
import { LayersRoutingModule } from './route/layers-routing.module';

@NgModule({
  imports: [SharedModule, LayersRoutingModule],
  declarations: [LayersComponent, LayersDetailComponent, LayersUpdateComponent, LayersDeleteDialogComponent],
})
export class LayersModule {}
