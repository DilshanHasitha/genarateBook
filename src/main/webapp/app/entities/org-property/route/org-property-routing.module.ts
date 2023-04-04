import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgPropertyComponent } from '../list/org-property.component';
import { OrgPropertyDetailComponent } from '../detail/org-property-detail.component';
import { OrgPropertyUpdateComponent } from '../update/org-property-update.component';
import { OrgPropertyRoutingResolveService } from './org-property-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const orgPropertyRoute: Routes = [
  {
    path: '',
    component: OrgPropertyComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgPropertyDetailComponent,
    resolve: {
      orgProperty: OrgPropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgPropertyUpdateComponent,
    resolve: {
      orgProperty: OrgPropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgPropertyUpdateComponent,
    resolve: {
      orgProperty: OrgPropertyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orgPropertyRoute)],
  exports: [RouterModule],
})
export class OrgPropertyRoutingModule {}
