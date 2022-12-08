import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PageLayersDetailsComponent } from '../list/page-layers-details.component';
import { PageLayersDetailsDetailComponent } from '../detail/page-layers-details-detail.component';
import { PageLayersDetailsUpdateComponent } from '../update/page-layers-details-update.component';
import { PageLayersDetailsRoutingResolveService } from './page-layers-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pageLayersDetailsRoute: Routes = [
  {
    path: '',
    component: PageLayersDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PageLayersDetailsDetailComponent,
    resolve: {
      pageLayersDetails: PageLayersDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PageLayersDetailsUpdateComponent,
    resolve: {
      pageLayersDetails: PageLayersDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PageLayersDetailsUpdateComponent,
    resolve: {
      pageLayersDetails: PageLayersDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pageLayersDetailsRoute)],
  exports: [RouterModule],
})
export class PageLayersDetailsRoutingModule {}
