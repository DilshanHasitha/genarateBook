import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PageSizeComponent } from './list/page-size.component';
import { PageSizeDetailComponent } from './detail/page-size-detail.component';
import { PageSizeUpdateComponent } from './update/page-size-update.component';
import { PageSizeDeleteDialogComponent } from './delete/page-size-delete-dialog.component';
import { PageSizeRoutingModule } from './route/page-size-routing.module';

@NgModule({
  imports: [SharedModule, PageSizeRoutingModule],
  declarations: [PageSizeComponent, PageSizeDetailComponent, PageSizeUpdateComponent, PageSizeDeleteDialogComponent],
})
export class PageSizeModule {}
