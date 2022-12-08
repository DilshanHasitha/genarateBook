import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PageLayersDetailsComponent } from './list/page-layers-details.component';
import { PageLayersDetailsDetailComponent } from './detail/page-layers-details-detail.component';
import { PageLayersDetailsUpdateComponent } from './update/page-layers-details-update.component';
import { PageLayersDetailsDeleteDialogComponent } from './delete/page-layers-details-delete-dialog.component';
import { PageLayersDetailsRoutingModule } from './route/page-layers-details-routing.module';

@NgModule({
  imports: [SharedModule, PageLayersDetailsRoutingModule],
  declarations: [
    PageLayersDetailsComponent,
    PageLayersDetailsDetailComponent,
    PageLayersDetailsUpdateComponent,
    PageLayersDetailsDeleteDialogComponent,
  ],
})
export class PageLayersDetailsModule {}
