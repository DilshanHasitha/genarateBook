import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PageLayersComponent } from '../list/page-layers.component';
import { PageLayersDetailComponent } from '../detail/page-layers-detail.component';
import { PageLayersUpdateComponent } from '../update/page-layers-update.component';
import { PageLayersRoutingResolveService } from './page-layers-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pageLayersRoute: Routes = [
  {
    path: '',
    component: PageLayersComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PageLayersDetailComponent,
    resolve: {
      pageLayers: PageLayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PageLayersUpdateComponent,
    resolve: {
      pageLayers: PageLayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PageLayersUpdateComponent,
    resolve: {
      pageLayers: PageLayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pageLayersRoute)],
  exports: [RouterModule],
})
export class PageLayersRoutingModule {}
