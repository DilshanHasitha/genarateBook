import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrgPropertyComponent } from './list/org-property.component';
import { OrgPropertyDetailComponent } from './detail/org-property-detail.component';
import { OrgPropertyUpdateComponent } from './update/org-property-update.component';
import { OrgPropertyDeleteDialogComponent } from './delete/org-property-delete-dialog.component';
import { OrgPropertyRoutingModule } from './route/org-property-routing.module';

@NgModule({
  imports: [SharedModule, OrgPropertyRoutingModule],
  declarations: [OrgPropertyComponent, OrgPropertyDetailComponent, OrgPropertyUpdateComponent, OrgPropertyDeleteDialogComponent],
})
export class OrgPropertyModule {}
