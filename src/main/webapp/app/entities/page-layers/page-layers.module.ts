import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PageLayersComponent } from './list/page-layers.component';
import { PageLayersDetailComponent } from './detail/page-layers-detail.component';
import { PageLayersUpdateComponent } from './update/page-layers-update.component';
import { PageLayersDeleteDialogComponent } from './delete/page-layers-delete-dialog.component';
import { PageLayersRoutingModule } from './route/page-layers-routing.module';

@NgModule({
  imports: [SharedModule, PageLayersRoutingModule],
  declarations: [PageLayersComponent, PageLayersDetailComponent, PageLayersUpdateComponent, PageLayersDeleteDialogComponent],
})
export class PageLayersModule {}
