import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LayersComponent } from '../list/layers.component';
import { LayersDetailComponent } from '../detail/layers-detail.component';
import { LayersUpdateComponent } from '../update/layers-update.component';
import { LayersRoutingResolveService } from './layers-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const layersRoute: Routes = [
  {
    path: '',
    component: LayersComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LayersDetailComponent,
    resolve: {
      layers: LayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LayersUpdateComponent,
    resolve: {
      layers: LayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LayersUpdateComponent,
    resolve: {
      layers: LayersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(layersRoute)],
  exports: [RouterModule],
})
export class LayersRoutingModule {}
